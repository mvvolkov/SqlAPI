package serverFileImpl;

import api.*;
import api.exceptions.ConstraintException;
import api.exceptions.NoSuchDatabaseException;
import api.exceptions.NoSuchTableException;
import api.exceptions.WrongValueTypeException;
import api.selectionPredicate.ColumnValuePredicate;
import api.selectionPredicate.Predicate;
import api.ResultRow;
import api.ResultSet;
import clientDefaultImpl.AssignmentOperationImpl;
import clientDefaultImpl.ColumnReferenceImpl;
import clientDefaultImpl.SelectedItemImpl;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class TableImpl implements Table, Serializable {

    private final DatabaseImpl database;

    private final TableMetadata metadata;

    private final List<Row> rows = new ArrayList<>();

    public TableImpl(DatabaseImpl database, TableMetadata metadata) {
        this.database = database;
        this.metadata = metadata;
    }


    @Override
    public TableMetadata getMetadata() {
        return metadata;
    }

    @Override
    public Database getDatabase() {
        return database;
    }

    @Override
    public void checkPrimaryKey(ColumnReference columnReference, Object value)
            throws ConstraintException, WrongValueTypeException {

        for (Row row : rows) {
            Value v2 = row.getValue(columnReference.getColumnName());
            if (v2.evaluate(this.getColumnValuePredicate(columnReference, value))) {
                throw new ConstraintException(columnReference, "PRIMARY KEY");
            }
        }
    }

    private ColumnValuePredicate getColumnValuePredicate(ColumnReference columnReference, Object value) {
        return new ColumnValuePredicate() {
            @Override
            public ColumnReference getColumnReference() {
                return columnReference;
            }

            @Override
            public Comparable getValue() {
                return (Comparable) value;
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

    private ColumnReferenceImpl createColumnReference(ColumnMetadata columnMetadata) {
        return new ColumnReferenceImpl(columnMetadata.getName(), metadata.getName(), database.getName());
    }

    protected void checkConstraints(ColumnMetadata columnMetadata, Object value)
            throws WrongValueTypeException, ConstraintException {
        if (value != null && !columnMetadata.getJavaClass().isInstance(value)) {
            throw new WrongValueTypeException(this.createColumnReference(columnMetadata),
                    columnMetadata.getJavaClass(), value.getClass());
        }
        if (value == null && columnMetadata.isNotNull()) {
            throw new ConstraintException(this.createColumnReference(columnMetadata), "NOT NULL");
        }
        if (columnMetadata.isPrimaryKey()) {

            for (Row row : rows) {
                Value v2 = row.getValue(columnMetadata.getName());
                if (v2.evaluate(this.getColumnValuePredicate(this.createColumnReference(columnMetadata), value))) {
                    throw new ConstraintException(this.createColumnReference(columnMetadata), "PRIMARY KEY");
                }
            }

        }
    }

    @Override
    public void insert(List<Object> values) throws WrongValueTypeException, ConstraintException {

        List<ColumnMetadata> columnsMetadata = metadata.getColumnsMetadata();
        Map<String, Value> map = new HashMap<>();
        for (int i = 0; i < columnsMetadata.size(); i++) {
            ColumnMetadata columnMetadata = columnsMetadata.get(i);
            Object value = values.size() > i ? values.get(i) : null;
            columnMetadata.checkConstraints(this, value);
            this.checkConstraints(columnMetadata, value);
            map.put(columnMetadata.getName(), new Value(columnMetadata.getJavaClass(), value));
        }
        rows.add(new Row(map));

        try {
            this.getLoggerTable().insert(values);
        } catch (NoSuchTableException e) {
            e.printStackTrace();
        } catch (NoSuchDatabaseException e) {
            e.printStackTrace();
        }
    }

    private Table getLoggerTable() throws NoSuchDatabaseException, NoSuchTableException {
        return database.getLoggerDatabase().getTable(metadata.getName());
    }

    @Override
    public void insert(List<String> columns, List<Object> values)
            throws WrongValueTypeException, ConstraintException {

    }

    @Override
    public void insert(ResultSet rows) {

    }

    @Override
    public void delete(Predicate selectionPredicate) {

    }

    @Override
    public void update(List<AssignmentOperationImpl> newValues, Predicate selectionPredicate) {

    }

    private ResultRow getSelectionResultRow(Row row, List<SelectedItemImpl> selectedItems) {
        List<Object> values = new ArrayList<>();
        List<String> columnNames = new ArrayList<>();
        if (selectedItems.isEmpty()) {
            for (ColumnMetadata columnMetadata : metadata.getColumnsMetadata()) {
                Value tableValue = row.getValue(columnMetadata.getName());
                values.add(tableValue.getValue());
                columnNames.add(columnMetadata.getName());
            }
            return new ResultRowImpl(columnNames, values);
        }
        for (SelectedItemImpl selectedItem : selectedItems) {
            switch (selectedItem.getType()) {
                case SELECT_ALL:
                    for (ColumnMetadata columnMetadata : metadata.getColumnsMetadata()) {
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
        return new ResultSetImpl(resultRows, new ArrayList<>(metadata.getColumnsMetadata().stream()
                .map(ColumnMetadata::getName).collect(Collectors.toList())));
    }

    @Override
    public ResultSet select(List<SelectedItemImpl> selectedItems, Predicate selectionPredicate)
            throws WrongValueTypeException {


        List<ResultRow> resultRows = new ArrayList<>();
        for (Row row : rows) {

            resultRows.add(this.getSelectionResultRow(row, selectedItems));

        }
        return new ResultSetImpl(resultRows,
                new ArrayList<>(metadata.getColumnsMetadata().stream()
                        .map(ColumnMetadata::getName).collect(Collectors.toList())));
    }
}
