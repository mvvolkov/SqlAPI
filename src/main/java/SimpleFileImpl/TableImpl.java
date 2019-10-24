package SimpleFileImpl;

import sqlapi.*;
import sqlapi.dbMetadata.ColumnMetadata;
import sqlapi.dbMetadata.TableMetadata;
import sqlapi.exceptions.ConstraintException;
import sqlapi.exceptions.WrongValueTypeException;
import sqlapi.selectionPredicate.AbstractPredicate;
import sqlapi.selectionPredicate.ColumnComparisonPredicate;
import sqlapi.selectionResult.SelectionResultRow;
import sqlapi.selectionResult.SelectionResultSet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TableImpl implements Table, Serializable {

    private final Database database;

    private final TableMetadata description;

    private final List<Row> rows = new ArrayList<>();

    public TableImpl(Database database, TableMetadata description) {
        this.database = database;
        this.description = description;
    }

    private void printOut() {
        List<ColumnMetadata> columns = description.getColumnMetadata();
        String tableString = columns.stream().map(ColumnMetadata::getColumnName)
                .collect(Collectors.joining(", "));
        System.out.println("\nTable " + description.getName() + ": ");
        System.out.println(tableString);
        for (Row row : rows) {
            String rowString = columns.stream().map(c -> row.getValue(c.getColumnName()).toString())
                    .collect(Collectors.joining(", "));
            System.out.println(rowString);
        }
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
                boolean b = v2.evaluate((ColumnComparisonPredicate) AbstractPredicate.equals(
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

        this.printOut();
    }

    @Override
    public void insert(List<String> columns, List<Object> values)
            throws WrongValueTypeException, ConstraintException {


    }

    @Override
    public void insert(SelectionResultSet rows) {

    }

    @Override
    public void delete(AbstractPredicate selectionPredicate) {

    }

    @Override
    public void update(List<AssignmentOperation> newValues, AbstractPredicate selectionPredicate) {

    }

    @Override
    public List<SelectionResultRow> select(List<SelectionUnit> selectionUnits, AbstractPredicate selectionPredicate) {
        return null;
    }
}
