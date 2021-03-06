package localFileDatabase.server.persistent;

import clientImpl.columnExpr.ColumnExprFactory;
import localFileDatabase.server.intermediate.ResultHeader;
import localFileDatabase.server.intermediate.ResultRow;
import localFileDatabase.server.intermediate.ResultValue;
import org.jetbrains.annotations.NotNull;
import sqlapi.columnExpr.InputValue;
import sqlapi.columnExpr.ParametrizedInputValue;
import sqlapi.exceptions.*;
import sqlapi.metadata.*;
import sqlapi.misc.AssignmentOperation;
import sqlapi.predicates.Predicate;
import sqlapi.queries.*;
import sqlapi.queryResult.QueryResult;
import sqlapi.queryResult.QueryResultRow;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public final class PersistentTable implements Serializable, TableMetadata {

    public static final long serialVersionUID = 9082226890498779849L;

    private final PersistentDatabase database;

    private final String tableName;

    private final List<PersistentColumnMetadata> columns = new ArrayList<>();

    private final Collection<PersistentRow> rows = new ArrayList<>();

    PersistentTable(TableMetadata tableMetadata, PersistentDatabase database)
            throws SqlException {
        this.database = database;
        this.tableName = tableMetadata.getTableName();
        for (ColumnMetadata columnMetadata : tableMetadata.getColumnsMetadata()) {
            switch (columnMetadata.getSqlType()) {
                case VARCHAR:
                    this.columns.add(new VarcharColumnMetadata(columnMetadata, this));
                    break;
                case INTEGER:
                    this.columns.add(new IntegerColumnMetadata(columnMetadata, this));
                    break;
                default:
                    throw new UnsupportedSqlTypeException(columnMetadata.getSqlType());
            }
        }
    }

    public void executeQuery(TableQuery query) throws SqlException {
        if (query instanceof InsertQuery) {
            this.insert((InsertQuery) query);
        } else if (query instanceof DeleteQuery) {
            this.delete((DeleteQuery) query);
        } else if (query instanceof UpdateQuery) {
            this.update((UpdateQuery) query);
        } else if (query instanceof InsertFromSelectQuery) {
            InsertFromSelectQuery insert = (InsertFromSelectQuery) query;
            this.insert(insert,
                    database.getServer().getQueryResult(insert.getSelectQuery()));
        } else {
            throw new UnsupportedQueryTypeException(query);
        }
    }

    String getDatabaseName() {
        return database.getDatabaseName();
    }

    @NotNull
    @Override
    public String getTableName() {
        return tableName;
    }

    @Override
    public @NotNull List<ColumnMetadata> getColumnsMetadata() {
        return new ArrayList<>(columns);
    }


    public PersistentColumnMetadata getColumnMetadata(String columnName)
            throws NoSuchColumnException {
        for (PersistentColumnMetadata cm : columns) {
            if (cm.getColumnName().equals(columnName)) {
                return cm;
            }
        }
        throw new NoSuchColumnException(columnName);
    }


    private void insert(InsertQuery query)
            throws SqlException {
        this.insert(query.getColumns(), query.getInputValues());
    }

    private void insert(InsertFromSelectQuery query, QueryResult queryResult)
            throws SqlException {
        for (QueryResultRow row : queryResult.getRows()) {
            List<InputValue> values = new ArrayList<>();
            for (Object value : row.getValues()) {
                values.add(ColumnExprFactory.value(value));
            }
            this.insert(query.getColumns(), values);
        }
    }

    private void delete(DeleteQuery query)
            throws SqlException {
        this.delete(query.getPredicate());
    }


    private void insert(List<String> columnNames, List<InputValue> values)
            throws SqlException {

        // check number of values
        int columnCount = columnNames.isEmpty() ? columns.size() : columnNames.size();
        if (columnCount != values.size()) {
            throw new InvalidQueryException(
                    "Invalid insert query. Number of values differs from number of columns");
        }

        PersistentRow row = new PersistentRow();

        // insert values
        for (int i = 0; i < columnCount; i++) {
            String columnName = columnNames.isEmpty() ? columns.get(i).getColumnName()
                    : columnNames.get(i);
            PersistentColumnMetadata cm = this.getColumnMetadata(columnName);
            InputValue inputValue = values.get(i);
            if (inputValue instanceof ParametrizedInputValue) {
                if (((ParametrizedInputValue) inputValue).isEmpty()) {
                    throw new MissingParameterException();
                }
            }
            Object value = cm.getCheckedValue(inputValue.getValue());
            row.setValue(columnName, value);
        }

        // insert default values for missing columns
        if (row.length() != columns.size()) {
            for (PersistentColumnMetadata columnMetadata : columns) {
                String columnName = columnMetadata.getColumnName();
                if (row.hasValue(columnName)) {
                    continue;
                }
                Object value = columnMetadata.getDefaultValue();
                row.setValue(columnName, value);
            }
        }
        rows.add(row);
    }


    private void delete(Predicate predicate)
            throws SqlException {
        if (predicate.isEmpty()) {
            rows.clear();
        }
        List<PersistentRow> rowsToDelete = new ArrayList<>();
        for (PersistentRow row : rows) {
            if (this.createResultRow(row).matchPredicate(predicate)) {
                rowsToDelete.add(row);
            }
        }
        rows.removeAll(rowsToDelete);
    }

    private void update(UpdateQuery query)
            throws SqlException {

        for (PersistentRow row : rows) {
            ResultRow resultRow = this.createResultRow(row);
            if (!resultRow.matchPredicate(query.getPredicate())) {
                continue;
            }
            for (AssignmentOperation ao : query.getAssignmentOperations()) {
                String columnName = ao.getColumnName();
                Object newValue = resultRow.evaluateColumnExpr(ao.getValue()).getValue();
                newValue = this.getColumnMetadata(columnName).getCheckedValue(newValue);
                row.setValue(columnName, newValue);
            }
        }
    }

    public void checkUniqueConstraint(Object newValue, String columnName,
                                      ColumnConstraintType type)
            throws ConstraintViolationException {
        for (PersistentRow row : rows) {
            if (row.getValue(columnName).equals(newValue)) {
                throw new ConstraintViolationException(this.getDatabaseName(), tableName,
                        columnName, type);
            }
        }
    }

    public void validate(List<ColumnMetadata> otherColumns)
            throws FailedDatabaseValidationException {
        if (columns.size() != otherColumns.size()) {
            throw new FailedDatabaseValidationException(
                    "number of columns for table " + database.getDatabaseName() + "."
                            + tableName + " is different");
        }
        for (ColumnMetadata cm : otherColumns) {
            try {
                this.getColumnMetadata(cm.getColumnName()).validate(cm);
            } catch (NoSuchColumnException e) {
                throw new FailedDatabaseValidationException(
                        "column " + cm.getColumnName() + " not found in the table " +
                                database.getDatabaseName() + "." + tableName);
            }
        }
    }


    public List<ResultHeader> getResultHeaders() {
        return columns.stream().map(this::createResultHeader).collect(
                Collectors.toList());
    }

    public List<ResultRow> getResultRows() {
        return rows.stream().map(this::createResultRow).collect(
                Collectors.toList());
    }

    private ResultRow createResultRow(PersistentRow row) {
        List<ResultValue> values = new ArrayList<>();
        for (PersistentColumnMetadata cm : columns) {
            values.add(new ResultValue(this.createResultHeader(cm),
                    row.getValue(cm.getColumnName())));
        }
        return new ResultRow(values);
    }

    private ResultHeader createResultHeader(PersistentColumnMetadata columnMetadata) {
        return new ResultHeader(this.getDatabaseName(), tableName,
                columnMetadata.getColumnName());
    }
}
