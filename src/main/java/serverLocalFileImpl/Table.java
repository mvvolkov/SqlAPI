package serverLocalFileImpl;

import api.ResultSet;
import api.columnExpr.ColumnRef;
import api.columnExpr.ColumnValue;
import api.exceptions.ConstraintException;
import api.exceptions.WrongValueTypeException;
import api.metadata.ColumnMetadata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Table implements Serializable {

    private final String dbName;

    private final String tableName;

    private final List<ColumnMetadata> columns;

    private final List<Row> rows = new ArrayList<>();

    public Table(String dbName, String tableName, List<ColumnMetadata> columns) {
        this.dbName = dbName;
        this.tableName = tableName;
        this.columns = columns;
    }

    public String getTableName() {
        return tableName;
    }

    public List<ColumnMetadata> getColumns() {
        return columns;
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

    protected void checkConstraints(ColumnMetadata columnMetadata,
                                    ColumnValue columnValue)
            throws WrongValueTypeException, ConstraintException {
        Object value = columnValue.getValue();
        if (value != null &&
                !columnMetadata.getJavaClass().isInstance(value)) {
            throw new WrongValueTypeException(this.createColumnRef(columnMetadata.getName()),
                    columnMetadata.getJavaClass(), value.getClass());
        }
        if (value == null && columnMetadata.isNotNull()) {
            throw new ConstraintException(this.createColumnRef(columnMetadata.getName()),
                    "NOT NULL");
        }
        if (columnMetadata.isPrimaryKey()) {

            for (Row row : rows) {
                Value v2 = row.getValue(columnMetadata.getName());
                if (v2.getValue().compareTo(columnValue.getValue()) == 0) {
                    throw new ConstraintException(
                            this.createColumnRef(columnMetadata.getName()), "PRIMARY KEY");
                }
            }
        }
    }


    public void insert(List<ColumnValue> values)
            throws WrongValueTypeException, ConstraintException {


        Map<String, Value> map = new HashMap<>();
        for (int i = 0; i < columns.size(); i++) {
            ColumnMetadata columnMetadata = columns.get(i);
            ColumnValue columnValue = values.size() > i ? values.get(i) : null;
            this.checkConstraints(columnMetadata, columnValue);
            map.put(columnMetadata.getName(),
                    new Value(columnMetadata.getJavaClass(), columnValue.getValue()));
        }
        rows.add(new Row(map));
    }


    public void insert(List<String> columns, List<ColumnValue> values)
            throws WrongValueTypeException, ConstraintException {

    }


    public void insert(ResultSet resultSet) {

    }


    protected InternalResultSet getData() {
        List<ColumnRef> resultColumns = new ArrayList<>();
        for (ColumnMetadata column : columns) {
            resultColumns.add(this.createColumnRef(column.getName()));
        }
        List<InternalResultRow> resultRows = new ArrayList<>();
        for (Row row : rows) {
            List<Value> values = new ArrayList<>();
            for (ColumnMetadata column : columns) {
                values.add(row.getValue(column.getName()));
            }
            resultRows.add(new InternalResultRow(resultColumns, values));
        }
        return new InternalResultSet(resultColumns, resultRows);
    }
}
