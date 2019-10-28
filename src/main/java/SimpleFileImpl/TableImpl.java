package SimpleFileImpl;

import sqlapi.AssignmentOperation;
import sqlapi.ColumnReference;
import sqlapi.SelectedColumn;
import sqlapi.Table;
import sqlapi.dbMetadata.ColumnMetadata;
import sqlapi.dbMetadata.TableMetadata;
import sqlapi.exceptions.ConstraintException;
import sqlapi.exceptions.NoSuchDatabaseException;
import sqlapi.exceptions.NoSuchTableException;
import sqlapi.exceptions.WrongValueTypeException;
import sqlapi.selectionPredicate.OneColumnPredicate;
import sqlapi.selectionPredicate.SelectionPredicate;
import sqlapi.selectionResult.ResultRow;
import sqlapi.selectionResult.ResultSet;
import sqlapi.selectionResult.ResultValue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableImpl implements Table, Serializable {

    private final DatabaseImpl database;

    private final TableMetadata description;

    private final List<Row> rows = new ArrayList<>();

    public TableImpl(DatabaseImpl database, TableMetadata description) {
        this.database = database;
        this.description = description;
    }


    @Override
    public TableMetadata getMetadata() {
        return description;
    }

    private ColumnReference createColumnReference(ColumnMetadata columnMetadata) {
        return new ColumnReference(columnMetadata.getColumnName(), description.getName(), database.getName());
    }

    protected void checkConstraints(ColumnMetadata columnMetadata, Object value)
            throws WrongValueTypeException, ConstraintException {
        if (value != null && !columnMetadata.getType().isInstance(value)) {
            throw new WrongValueTypeException(this.createColumnReference(columnMetadata),
                    columnMetadata.getType(), value.getClass());
        }
        if (value == null && columnMetadata.isNotNull()) {
            throw new ConstraintException(this.createColumnReference(columnMetadata), "NOT NULL");
        }
        if (columnMetadata.isPrimaryKey()) {

            for (Row row : rows) {
                Value v2 = row.getValue(columnMetadata.getColumnName());
                boolean b = v2.evaluate((OneColumnPredicate) SelectionPredicate.equals(
                        this.createColumnReference(columnMetadata), value));
                if (b) {
                    throw new ConstraintException(this.createColumnReference(columnMetadata), "PRIMARY KEY");
                }
            }

        }
    }

    @Override
    public void insert(List<Object> values) throws WrongValueTypeException, ConstraintException {

        List<ColumnMetadata> columnsMetadata = description.getColumnMetadata();
        Map<String, Value> map = new HashMap<>();
        for (int i = 0; i < columnsMetadata.size(); i++) {
            ColumnMetadata columnMetadata = columnsMetadata.get(i);
            Object value = values.size() > i ? values.get(i) : null;
            this.checkConstraints(columnMetadata, value);
            map.put(columnMetadata.getColumnName(), new Value(columnMetadata.getType(), value));
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
        return database.getLoggerDatabase().getTable(description.getName());
    }

    @Override
    public void insert(List<String> columns, List<Object> values)
            throws WrongValueTypeException, ConstraintException {

    }

    @Override
    public void insert(ResultSet rows) {

    }

    @Override
    public void delete(SelectionPredicate selectionPredicate) {

    }

    @Override
    public void update(List<AssignmentOperation> newValues, SelectionPredicate selectionPredicate) {

    }

    private ResultRow getSelectionResultRow(Row row, List<SelectedColumn> selectedColumns) {
        List<ResultValue> values = new ArrayList<>();
        if (selectedColumns.isEmpty()) {
            for (ColumnMetadata columnMetadata : description.getColumnMetadata()) {
                Value tableValue = row.getValue(columnMetadata.getColumnName());
                ResultValueImpl value = new ResultValueImpl(tableValue, columnMetadata.getColumnName());
                values.add(value);
            }
        }
        for (SelectedColumn selectedColumn : selectedColumns) {
            switch (selectedColumn.getType()) {
                case SELECT_ALL:
                    for (ColumnMetadata columnMetadata : description.getColumnMetadata()) {
                        Value tableValue = row.getValue(columnMetadata.getColumnName());
                        ResultValueImpl value = new ResultValueImpl(tableValue, columnMetadata.getColumnName());
                        values.add(value);
                    }
                    break;
                case SELECT_ALL_FROM_TABLE:
                    break;
                case SELECT_COLUMN_EXPRESSION:
                    break;
            }


        }
        return new ResultRowImpl(values);
    }

    @Override
    public ResultSet select(List<SelectedColumn> selectedColumns, SelectionPredicate selectionPredicate) throws WrongValueTypeException {


        List<ResultRow> resultRows = new ArrayList<>();
        for (Row row : rows) {

            resultRows.add(this.getSelectionResultRow(row, selectedColumns));

        }
        return new ResultSetImpl(resultRows, new ArrayList<>(description.getColumnMetadata()));
    }
}
