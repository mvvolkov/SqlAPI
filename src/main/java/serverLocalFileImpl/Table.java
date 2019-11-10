package serverLocalFileImpl;

import api.ResultRow;
import api.ResultSet;
import api.columnExpr.ColumnRef;
import api.columnExpr.ColumnValue;
import api.exceptions.ConstraintException;
import api.exceptions.WrongValueTypeException;
import api.metadata.ColumnMetadata;
import api.predicates.ColumnValuePredicate;
import api.predicates.Predicate;
import clientImpl.selectedItems.SelectedItemImpl;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class Table implements Serializable {

    private final Database database;


    private final String tableName;

    private final List<ColumnMetadata> columns;

    private final List<Row> rows = new ArrayList<>();

    public Table(Database database, String tableName, List<ColumnMetadata> columns) {
        this.database = database;
        this.tableName = tableName;
        this.columns = columns;
    }

    public String getTableName() {
        return tableName;
    }

    public List<ColumnMetadata> getColumns() {
        return columns;
    }


//    public void checkPrimaryKey(ColumnRef columnRef, Object value)
//            throws ConstraintException, WrongValueTypeException {
//
//        for (Row row : rows) {
//            Value v2 = row.getValue(columnRef.getColumnName());
//            if (v2.evaluate(this.getColumnValuePredicate(columnRef, value))) {
//                throw new ConstraintException(columnRef, "PRIMARY KEY");
//            }
//        }
//    }

    private ColumnValuePredicate getColumnValuePredicate(ColumnRef columnRef,
                                                         ColumnValue value) {
        return new ColumnValuePredicate() {
            @Override
            public ColumnRef getColumnRef() {
                return columnRef;
            }

            @Override
            public ColumnValue getColumnValue() {
                return value;
            }

            @Override
            public Type getType() {
                return Type.EQUALS;
            }

            @Override
            public Predicate and(Predicate predicate) {
                return null;
            }

            @Override
            public Predicate or(Predicate predicate) {
                return null;
            }
        };
    }

    private ColumnRef createColumnReference(ColumnMetadata columnMetadata) {
        return new ColumnRef() {

            @Override public String getColumnName() {
                return columnMetadata.getName();
            }

            @Override public String getTableName() {
                return tableName;
            }

            @Override public String getDatabaseName() {
                return database.getName();
            }
        };
    }

    protected void checkConstraints(ColumnMetadata columnMetadata,
                                    ColumnValue columnValue)
            throws WrongValueTypeException, ConstraintException {
        Object value = columnValue.getValue();
        if (value != null &&
                !columnMetadata.getJavaClass().isInstance(value)) {
            throw new WrongValueTypeException(this.createColumnReference(columnMetadata),
                    columnMetadata.getJavaClass(), value.getClass());
        }
        if (value == null && columnMetadata.isNotNull()) {
            throw new ConstraintException(this.createColumnReference(columnMetadata),
                    "NOT NULL");
        }
        if (columnMetadata.isPrimaryKey()) {

            for (Row row : rows) {
                Value v2 = row.getValue(columnMetadata.getName());
                if (v2.evaluate(this.getColumnValuePredicate(
                        this.createColumnReference(columnMetadata), columnValue))) {
                    throw new ConstraintException(
                            this.createColumnReference(columnMetadata), "PRIMARY KEY");
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
//            columnMetadata.checkConstraints(this, value);
            this.checkConstraints(columnMetadata, columnValue);
            map.put(columnMetadata.getName(),
                    new Value(columnMetadata.getJavaClass(), columnValue.getValue()));
        }
        rows.add(new Row(map));
    }


    public void insert(List<String> columns, List<ColumnValue> values)
            throws WrongValueTypeException, ConstraintException {

    }


    public void insert(ResultSet rows) {

    }


    private ResultRow getSelectionResultRow(Row row,
                                            List<SelectedItemImpl> selectedItems) {
        List<Object> values = new ArrayList<>();
        List<String> columnNames = new ArrayList<>();
        if (selectedItems.isEmpty()) {
            for (ColumnMetadata columnMetadata : columns) {
                Value tableValue = row.getValue(columnMetadata.getName());
                values.add(tableValue.getValue());
                columnNames.add(columnMetadata.getName());
            }
            return new ResultRowImpl(columnNames, values);
        }
        for (SelectedItemImpl selectedItem : selectedItems) {
            switch (selectedItem.getType()) {
                case SELECT_ALL:
                    for (ColumnMetadata columnMetadata : columns) {
                        Value tableValue = row.getValue(columnMetadata.getName());
                        values.add(tableValue.getValue());
                        columnNames.add(columnMetadata.getName());
                    }
                    break;
                case SELECT_ALL_FROM_TABLE:
                    break;
                case SELECT_COLUMN_EXPRESSION:
                    break;
            }
        }
        return new ResultRowImpl(columnNames, values);
    }

    protected ResultSet selectAll() {
        List<ResultRow> resultRows = new ArrayList<>();
        for (Row row : rows) {

            resultRows.add(this.getSelectionResultRow(row, Collections.EMPTY_LIST));

        }
        return new ResultSetImpl(resultRows, new ArrayList<>(columns.stream()
                .map(ColumnMetadata::getName).collect(Collectors.toList())));
    }


    public ResultSet select(List<SelectedItemImpl> selectedItems,
                            Predicate selectionPredicate)
            throws WrongValueTypeException {


        List<ResultRow> resultRows = new ArrayList<>();
        for (Row row : rows) {

            resultRows.add(this.getSelectionResultRow(row, selectedItems));

        }
        return new ResultSetImpl(resultRows,
                new ArrayList<>(columns.stream()
                        .map(ColumnMetadata::getName).collect(Collectors.toList())));
    }
}
