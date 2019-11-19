package serverLocalFileImpl.persistent;

import api.columnExpr.ColumnRef;
import api.exceptions.*;
import api.metadata.ColumnMetadata;
import api.misc.AssignmentOperation;
import api.predicates.Predicate;
import api.queries.UpdateStatement;
import serverLocalFileImpl.ColumnRefImpl;
import serverLocalFileImpl.InternalResultRow;
import serverLocalFileImpl.InternalResultSet;

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
                    c.getSqlType(), c.isNotNull(),
                    c.isPrimaryKey(), c.getSize(), c.getDefaultValue(), this));
        }
    }

    public String getTableName() {
        return tableName;
    }

    public List<PersistentColumnMetadata> getColumns() {
        return columns;
    }

    private PersistentColumnMetadata getColumnMetadate(String columnName) throws NoSuchColumnException {
        for (PersistentColumnMetadata cm : columns) {
            if (cm.getColumnName().equals(columnName)) {
                return cm;
            }
        }
        throw new NoSuchColumnException(columnName);
    }


    public void insert(List<String> columnNames, List<Object> values)
            throws WrongValueTypeException, ConstraintException, InvalidQueryException {


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
            if (this.getInternalResultRow(row).matchPredicate(predicate)) {
                rowsToDelete.add(row);
            }
        }
        rows.removeAll(rowsToDelete);
    }

    void update(UpdateStatement stmt)
            throws SqlException {

        for (PersistentRow row : rows) {
            InternalResultRow irr = this.getInternalResultRow(row);
            if (irr.matchPredicate(stmt.getPredicate())) {
                for (AssignmentOperation ao : stmt.getAssignmentOperations()) {
                    String columnName = ao.getColumnName();
                    Object newValue = irr.evaluateColumnExpr(ao.getValue());
                    this.checkConstraints(this.getColumnMetadate(columnName), newValue);
                    row.getValues().put(columnName, newValue);
                }
            }
        }
    }


    private InternalResultRow getInternalResultRow(PersistentRow row) {
        Map<ColumnRef, Object> values = new HashMap<>();
        for (String columnName : row.getValues().keySet()) {
            values.put(this.createColumnRef(columnName),
                    row.getValues().get(columnName));
        }
        return new InternalResultRow(values);
    }


    private void checkConstraints(PersistentColumnMetadata cm,
                                  Object newValue)
            throws WrongValueTypeException, ConstraintException {

        cm.checkValueType(newValue);

        if (newValue == null && cm.isNotNull()) {
            throw new ConstraintException(schemaName, tableName,
                    cm.getColumnName(), "NOT NULL");
        }

        if (cm.isPrimaryKey()) {
            for (PersistentRow row : rows) {
                if (row.getValue(cm.getColumnName()).equals(newValue)) {
                    throw new ConstraintException(schemaName, tableName,
                            cm.getColumnName(), "PRIMARY KEY");
                }
            }
        }
        if (cm.getSize() != -1 && newValue != null) {
            String strValue = (String) newValue;
            if (strValue.length() > cm.getSize()) {
                throw new ConstraintException(schemaName, tableName,
                        cm.getColumnName(), "SIZE EXCEEDED");
            }
        }
    }


    public InternalResultSet getData() {
        List<ColumnRef> resultColumns = new ArrayList<>();
        for (PersistentColumnMetadata column : columns) {
            resultColumns.add(this.createColumnRef(column.getColumnName()));
        }
        List<InternalResultRow> resultRows = new ArrayList<>();
        for (PersistentRow row : rows) {
            resultRows.add(this.getInternalResultRow(row));
        }
        return new InternalResultSet(resultColumns, resultRows);
    }

    private ColumnRef createColumnRef(String columnName) {
        return new ColumnRefImpl(schemaName, tableName, columnName);
    }

}
