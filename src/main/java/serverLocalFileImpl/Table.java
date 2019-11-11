package serverLocalFileImpl;

import api.ResultSet;
import api.columnExpr.ColumnRef;
import api.columnExpr.ColumnValue;
import api.exceptions.ConstraintException;
import api.exceptions.WrongValueTypeException;
import api.metadata.ColumnMetadata;
import api.queries.DeleteStatement;
import api.queries.UpdateStatement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Table implements Serializable {

    public static final long serialVersionUID = 9082226890498779849L;

    private final String dbName;

    private final String tableName;

    private final List<InternalColumnMetadata> columns = new ArrayList<>();

    private final List<Row> rows = new ArrayList<>();

    public Table(String dbName, String tableName, List<ColumnMetadata<?>> columns) {
        this.dbName = dbName;
        this.tableName = tableName;
        for (ColumnMetadata c : columns) {
            this.columns.add(new InternalColumnMetadata(c.getColumnName(),
                    c.getSqlTypeName(), c.getJavaClass(), c.isNotNull(),
                    c.isPrimaryKey(), c.getSize()));
        }
    }

    public String getTableName() {
        return tableName;
    }

    public List<InternalColumnMetadata> getColumns() {
        return columns;
    }

    public void insert(List<ColumnValue<?>> values)
            throws WrongValueTypeException, ConstraintException {


        Map<String, Value> map = new HashMap<>();
        for (int i = 0; i < columns.size(); i++) {
            InternalColumnMetadata columnMetadata = columns.get(i);
            ColumnValue<?> columnValue = values.size() > i ? values.get(i) : null;
            this.checkConstraints(columnMetadata, columnValue);
            map.put(columnMetadata.getColumnName(),
                    new Value(columnMetadata.getJavaClass(), columnValue.getValue()));
        }
        rows.add(new Row(map));
    }


    public void insert(List<String> columns, List<ColumnValue<?>> values)
            throws WrongValueTypeException, ConstraintException {

    }


    public void insert(ResultSet resultSet) {

    }

    public void delete(DeleteStatement stmt) {

    }

    public void update(UpdateStatement stmt) {

    }


    private void checkConstraints(InternalColumnMetadata cm,
                                  ColumnValue cv)
            throws WrongValueTypeException, ConstraintException {
        Object value = cv.getValue();
        if (value != null &&
                !cm.getJavaClass().isInstance(value)) {
            throw new WrongValueTypeException(
                    this.createColumnRef(cm.getColumnName()),
                    cm.getJavaClass(), value.getClass());
        }
        if (value == null && cm.isNotNull()) {
            throw new ConstraintException(dbName, tableName,
                    cm.getColumnName(), "NOT NULL");
        }
        if (cm.isPrimaryKey()) {
            for (Row row : rows) {
                Value v2 = row.getValue(cm.getColumnName());
                if (v2.getValue().compareTo(cv.getValue()) == 0) {
                    throw new ConstraintException(dbName, tableName,
                            cm.getColumnName(), "PRIMARY KEY");
                }
            }
        }
    }


    InternalResultSet getData() {
        List<ColumnRef> resultColumns = new ArrayList<>();
        for (InternalColumnMetadata column : columns) {
            resultColumns.add(this.createColumnRef(column.getColumnName()));
        }
        List<InternalResultRow> resultRows = new ArrayList<>();
        for (Row row : rows) {
            List<Value> values = new ArrayList<>();
            for (InternalColumnMetadata column : columns) {
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
