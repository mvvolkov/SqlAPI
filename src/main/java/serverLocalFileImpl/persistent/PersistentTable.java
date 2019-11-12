package serverLocalFileImpl.persistent;

import api.ResultSet;
import api.columnExpr.ColumnRef;
import api.exceptions.ConstraintException;
import api.exceptions.InconsistentInsertStmtException;
import api.exceptions.WrongValueTypeException;
import api.metadata.ColumnMetadata;
import api.queries.DeleteStatement;
import api.queries.UpdateStatement;
import serverLocalFileImpl.InternalResultRow;
import serverLocalFileImpl.InternalResultSet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class PersistentTable implements Serializable {

    public static final long serialVersionUID = 9082226890498779849L;

    private final String dbName;

    private final String tableName;

    private final List<PersistentColumnMetadata> columns = new ArrayList<>();

    private final List<PersistentRow> rows = new ArrayList<>();


    public PersistentTable(String dbName, String tableName, List<ColumnMetadata<?>> columns) {
        this.dbName = dbName;
        this.tableName = tableName;
        for (ColumnMetadata c : columns) {
            this.columns.add(new PersistentColumnMetadata(c.getColumnName(),
                    c.getSqlTypeName(), c.getJavaClass(), c.isNotNull(),
                    c.isPrimaryKey(), c.getSize(), c.getDefaultValue()));
        }
    }

    public String getTableName() {
        return tableName;
    }

    public List<PersistentColumnMetadata> getColumns() {
        return columns;
    }


    public void insert(List<Object> values)
            throws WrongValueTypeException, ConstraintException {
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < columns.size(); i++) {
            PersistentColumnMetadata columnMetadata = columns.get(i);
            Object value = values.size() > i ? values.get(i) : null;
            this.checkConstraints(columnMetadata, value);
            map.put(columnMetadata.getColumnName(), value);
        }
        rows.add(new PersistentRow(map));
    }


    public void insert(List<String> columnNames, List<Object> values)
            throws WrongValueTypeException, ConstraintException, InconsistentInsertStmtException {


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
                throw new InconsistentInsertStmtException();
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


    public void insert(ResultSet resultSet) {

    }

    public void delete(DeleteStatement stmt) {

    }

    public void update(UpdateStatement stmt) {

    }


    private void checkConstraints(PersistentColumnMetadata cm,
                                  Object newValue)
            throws WrongValueTypeException, ConstraintException {

        if (newValue == null && cm.isNotNull()) {
            throw new ConstraintException(dbName, tableName,
                    cm.getColumnName(), "NOT NULL");
        }
        if (newValue != null && !cm.getJavaClass().isInstance(newValue)) {
            throw new WrongValueTypeException(
                    this.createColumnRef(cm.getColumnName()),
                    cm.getJavaClass(), newValue.getClass());
        }
        if (cm.isPrimaryKey()) {
            for (PersistentRow row : rows) {
                if (row.getValue(cm.getColumnName()).equals(newValue)) {
                    throw new ConstraintException(dbName, tableName,
                            cm.getColumnName(), "PRIMARY KEY");
                }
            }
        }
        if (cm.getSize() != -1) {
            String strValue = (String) newValue;
            if (strValue.length() > cm.getSize()) {
                throw new ConstraintException(dbName, tableName,
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
            List<Object> values = new ArrayList<>();
            for (PersistentColumnMetadata column : columns) {
                values.add(row.getValue(column.getColumnName()));
            }
            resultRows.add(new InternalResultRow(resultColumns, values));
        }
        return new InternalResultSet(resultColumns, resultRows);
    }

    private ColumnRef createColumnRef(String columnName) {
        return new ColumnRef() {

            @Override
            public String getColumnName() {
                return columnName;
            }

            @Override
            public String getTableName() {
                return tableName;
            }

            @Override
            public String getDatabaseName() {
                return dbName;
            }
        };
    }
}
