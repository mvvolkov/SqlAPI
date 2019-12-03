package localFileDatabase.server.persistent;

import clientImpl.columnExpr.ColumnExprFactory;
import localFileDatabase.server.intermediate.ResultHeader;
import localFileDatabase.server.intermediate.ResultRow;
import localFileDatabase.server.intermediate.ResultValue;
import org.jetbrains.annotations.NotNull;
import sqlapi.columnExpr.ColumnValue;
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

    private final String databaseName;

    private final String tableName;

    private final List<PersistentColumnMetadata> columns = new ArrayList<>();

    private final Collection<PersistentRow> rows = new ArrayList<>();

    PersistentTable(String databaseName, TableMetadata tableMetadata)
            throws WrongValueTypeException {
        this.databaseName = databaseName;
        this.tableName = tableMetadata.getTableName();
        for (ColumnMetadata columnMetadata : tableMetadata.getColumnsMetadata()) {
            this.columns.add(new PersistentColumnMetadata(columnMetadata));
        }
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

    public List<PersistentColumnMetadata> getColumns() {
        return columns;
    }

    private PersistentColumnMetadata getColumnMetadata(String columnName)
            throws NoSuchColumnException {
        for (PersistentColumnMetadata cm : columns) {
            if (cm.getColumnName().equals(columnName)) {
                return cm;
            }
        }
        throw new NoSuchColumnException(columnName);
    }

    public void executeQuery(TableActionQuery query) throws SqlException {
        if (query instanceof InsertQuery) {
            this.insert((InsertQuery) query);
            return;
        }
        if (query instanceof DeleteQuery) {
            this.delete((DeleteQuery) query);
            return;
        }
        if (query instanceof UpdateQuery) {
            this.update((UpdateQuery) query);
            return;
        }
        throw new UnsupportedQueryTypeException(query);
    }


    private void insert(InsertQuery query)
            throws SqlException {
        this.insert(query.getColumns(), query.getValues());
    }

    public void insert(InsertFromSelectQuery query, QueryResult queryResult)
            throws SqlException {
        for (QueryResultRow row : queryResult.getRows()) {
            List<ColumnValue> values = new ArrayList<>();
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


    public void insert(List<String> columnNames, List<ColumnValue> values)
            throws WrongValueTypeException, ConstraintViolationException,
            InvalidQueryException, UnsupportedColumnConstraintTypeException,
            NoSuchColumnException {

        // check number of values
        int columnCount = columnNames.isEmpty() ? columns.size() : columnNames.size();
        if (columnCount != values.size()) {
            throw new InvalidQueryException(
                    "Invalid insert query. Number of values differs from number of columns");
        }

        Map<String, Object> columnToValueMap = new HashMap<>();

        // insert values
        for (int i = 0; i < columnCount; i++) {
            String columnName = columnNames.isEmpty() ? columns.get(i).getColumnName()
                    : columnNames.get(i);
            Object value = values.get(i).getValue();
            this.checkConstraints(this.getColumnMetadata(columnName), value);
            columnToValueMap.put(columnName, value);
        }

        // insert default values for missing columns
        if (columnToValueMap.size() != columns.size()) {
            for (PersistentColumnMetadata columnMetadata : columns) {
                String columnName = columnMetadata.getColumnName();
                if (columnToValueMap.containsKey(columnName)) {
                    continue;
                }
                Object value = columnMetadata.getDefaultValue();
                this.checkConstraints(columnMetadata, value);
                columnToValueMap.put(columnName, value);
            }
        }
        rows.add(new PersistentRow(columnToValueMap));
    }


    void delete(Predicate predicate)
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

    void update(UpdateQuery query)
            throws SqlException {

        for (PersistentRow row : rows) {
            ResultRow irr = this.createResultRow(row);
            if (irr.matchPredicate(query.getPredicate())) {
                for (AssignmentOperation ao : query.getAssignmentOperations()) {
                    String columnName = ao.getColumnName();
                    Object newValue = irr.evaluateColumnExpr(ao.getValue()).getValue();
                    this.checkConstraints(this.getColumnMetadata(columnName), newValue);
                    row.getValues().put(columnName, newValue);
                }
            }
        }
    }


    private void checkNotNullConstraint(Object newValue, String ColumnName,
                                        ColumnConstraintType type)
            throws ConstraintViolationException {
        if (newValue == null) {
            throw new ConstraintViolationException(databaseName, tableName,
                    ColumnName, type);
        }
    }

    private void checkUniqueConstraint(Object newValue, String columnName,
                                       ColumnConstraintType type)
            throws ConstraintViolationException {
        for (PersistentRow row : rows) {
            if (row.getValue(columnName).equals(newValue)) {
                throw new ConstraintViolationException(databaseName, tableName,
                        columnName, type);
            }
        }
    }

    private void checkMaxSize(Object newValue, PersistentColumnMetadata cm,
                              ColumnConstraintType type)
            throws ConstraintViolationException,
            UnsupportedColumnConstraintTypeException {
        if (cm.getSize() != -1 && newValue != null) {
            if (cm.getSqlType() == SqlType.VARCHAR) {
                String strValue = (String) newValue;
                if (strValue.length() > cm.getSize()) {
                    throw new ConstraintViolationException(databaseName, tableName,
                            cm.getColumnName(), type);
                }
                return;
            }
            throw new UnsupportedColumnConstraintTypeException(type);
        }
    }

    private void checkConstraints(PersistentColumnMetadata cm,
                                  Object newValue)
            throws WrongValueTypeException, ConstraintViolationException,
            UnsupportedColumnConstraintTypeException {

        cm.checkValueType(newValue);

        for (ColumnConstraint constraint : cm.getConstraints()) {
            ColumnConstraintType type = constraint.getConstraintType();
            switch (type) {
                case NOT_NULL:
                    this.checkNotNullConstraint(newValue, cm.getColumnName(), type);
                    break;
                case UNIQUE:
                    this.checkUniqueConstraint(newValue, cm.getColumnName(), type);
                    break;
                case PRIMARY_KEY:
                    this.checkNotNullConstraint(newValue, cm.getColumnName(), type);
                    this.checkUniqueConstraint(newValue,
                            cm.getColumnName(), type);
                    break;
                case MAX_SIZE:
                    this.checkMaxSize(newValue, cm, type);
                    break;
                case DEFAULT_VALUE:
                    break;
                default:
                    throw new UnsupportedColumnConstraintTypeException(type);
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
            values.add(new ResultValue(this.createResultHeader(cm), row.getValue(cm.getColumnName())));
        }
        return new ResultRow(values);
    }

    private ResultHeader createResultHeader(PersistentColumnMetadata columnMetadata) {
        return new ResultHeader(databaseName, tableName,
                columnMetadata.getColumnName());
    }

    void validate(TableMetadata tableMetadata)
            throws NoSuchTableException, NoSuchColumnException {
        if (columns.size() != tableMetadata.getColumnsMetadata().size()) {
            throw new NoSuchTableException(tableName);
        }
        for (ColumnMetadata cm : tableMetadata.getColumnsMetadata()) {
            PersistentColumnMetadata columnMetadata =
                    this.getColumnMetadata(cm.getColumnName());
            columnMetadata.validate(cm);
        }
    }
}
