package serverLocalFileImpl.persistent;

import serverLocalFileImpl.intermediateresult.DataHeader;
import serverLocalFileImpl.intermediateresult.DataRow;
import serverLocalFileImpl.intermediateresult.DataSet;
import sqlapi.exceptions.*;
import sqlapi.metadata.ColumnConstraint;
import sqlapi.metadata.ColumnConstraintType;
import sqlapi.metadata.ColumnMetadata;
import sqlapi.metadata.SqlType;
import sqlapi.misc.AssignmentOperation;
import sqlapi.predicates.Predicate;
import sqlapi.queries.UpdateStatement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class PersistentTable implements Serializable {

    public static final long serialVersionUID = 9082226890498779849L;

    private final String schemaName;

    private final String tableName;

    private final List<PersistentColumnMetadata> columns = new ArrayList<>();

    private final List<PersistentRow> rows = new ArrayList<>();

    public PersistentTable(String schemaName, String tableName,
                           List<ColumnMetadata> columns) throws WrongValueTypeException {
        this.schemaName = schemaName;
        this.tableName = tableName;
        for (ColumnMetadata c : columns) {
            this.columns.add(new PersistentColumnMetadata(c.getColumnName(),
                    c.getSqlType(), c.getSize(), c.getConstraints(), this));
        }
    }

    public String getTableName() {
        return tableName;
    }

    public List<PersistentColumnMetadata> getColumns() {
        return columns;
    }

    private PersistentColumnMetadata getColumnMetadate(String columnName)
            throws NoSuchColumnException {
        for (PersistentColumnMetadata cm : columns) {
            if (cm.getColumnName().equals(columnName)) {
                return cm;
            }
        }
        throw new NoSuchColumnException(columnName);
    }


    public void insert(List<String> columnNames, List<Object> values)
            throws WrongValueTypeException, ConstraintViolationException, InvalidQueryException, MaxSizeExceededException {


        Map<String, Object> resultMap = new HashMap<>();
        if (columnNames.isEmpty()) {
            for (int i = 0; i < columns.size(); i++) {
                PersistentColumnMetadata columnMetadata = columns.get(i);
                Object value = values.size() > i ? values.get(i) : null;
                this.checkConstraints(columnMetadata, value);
                resultMap.put(columnMetadata.getColumnName(), value);
            }
        } else {
            if (columnNames.size() != values.size()) {
                throw new InvalidQueryException(
                        "Invalid insert query. Number of values differs from number of columns");
            }
            Map<String, Object> insertMap = new HashMap<>();
            for (int i = 0; i < columnNames.size(); i++) {
                insertMap.put(columnNames.get(i), values.get(i));
            }

            for (PersistentColumnMetadata columnMetadata : columns) {
                String columnName = columnMetadata.getColumnName();
                Object value;
                if (insertMap.containsKey(columnName)) {
                    value = insertMap.get(columnName);
                } else {
                    value = columnMetadata.getDefaultValue();
                }
                this.checkConstraints(columnMetadata, value);
                resultMap.put(columnName, value);
            }
        }
        rows.add(new PersistentRow(resultMap));
    }


    void delete(Predicate predicate)
            throws SqlException {
        if (predicate.isEmpty()) {
            rows.clear();
        }
        List<PersistentRow> rowsToDelete = new ArrayList<>();
        for (PersistentRow row : rows) {
            if (this.getDataRow(row).evaluatePredicate(predicate)) {
                rowsToDelete.add(row);
            }
        }
        rows.removeAll(rowsToDelete);
    }

    void update(UpdateStatement stmt)
            throws SqlException {

        for (PersistentRow row : rows) {
            DataRow irr = this.getDataRow(row);
            if (irr.evaluatePredicate(stmt.getPredicate())) {
                for (AssignmentOperation ao : stmt.getAssignmentOperations()) {
                    String columnName = ao.getColumnName();
                    Object newValue = irr.evaluateColumnExpr(ao.getValue());
                    this.checkConstraints(this.getColumnMetadate(columnName), newValue);
                    row.getValues().put(columnName, newValue);
                }
            }
        }
    }


    private DataRow getDataRow(PersistentRow row) {
        Map<DataHeader, Object> values = new HashMap<>();
        for (PersistentColumnMetadata cm : columns) {
            values.put(this.createColumnRef(cm.getColumnName(), cm.getSqlType()),
                    row.getValues().get(cm.getColumnName()));
        }
        return new DataRow(values);
    }

    private void checkNotNullConstraint(Object newValue, String ColumnName, ColumnConstraintType type) throws ConstraintViolationException {
        if (newValue == null) {
            throw new ConstraintViolationException(schemaName, tableName,
                    ColumnName, type);
        }
    }

    private void checkUniqueConstraint(Object newValue, String columnName, ColumnConstraintType type) throws ConstraintViolationException {
        for (PersistentRow row : rows) {
            if (row.getValue(columnName).equals(newValue)) {
                throw new ConstraintViolationException(schemaName, tableName,
                        columnName, type);
            }
        }
    }

    private void checkConstraints(PersistentColumnMetadata cm,
                                  Object newValue)
            throws WrongValueTypeException, ConstraintViolationException, MaxSizeExceededException {

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
                    this.checkUniqueConstraint(newValue, cm.getColumnName(), type);
                    break;
            }
        }


        if (cm.getSize() != -1 && newValue != null) {
            String strValue = (String) newValue;
            if (strValue.length() > cm.getSize()) {
                throw new MaxSizeExceededException(schemaName, tableName,
                        cm.getColumnName(), cm.getSize(), strValue.length());
            }
        }
    }


    public DataSet getData() {
        List<DataHeader> headers = new ArrayList<>();
        for (PersistentColumnMetadata column : columns) {
            headers.add(
                    this.createColumnRef(column.getColumnName(), column.getSqlType()));
        }
        List<DataRow> resultRows = new ArrayList<>();
        for (PersistentRow row : rows) {
            resultRows.add(this.getDataRow(row));
        }
        return new DataSet(headers, resultRows);
    }

    private DataHeader createColumnRef(String columnName,
                                       SqlType sqlType) {
        return new DataHeader(sqlType, schemaName, tableName, columnName);
    }

}
