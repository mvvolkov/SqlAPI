package SimpleFileImpl;

import sqlapi.*;
import sqlapi.dbMetadata.ColumnMetadata;
import sqlapi.dbMetadata.TableMetadata;
import sqlapi.exceptions.ConstraintException;
import sqlapi.exceptions.WrongValueTypeException;
import sqlapi.selectionPredicate.AbstractPredicate;
import sqlapi.selectionResult.SelectionResultRow;
import sqlapi.selectionResult.SelectionResultSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableImpl implements Table {

    private final Database database;

    private final TableMetadata description;

    private final List<Row> rows = new ArrayList<>();

    public TableImpl(Database database, TableMetadata description) {
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

    @Override
    public void insert(List<Object> values) throws WrongValueTypeException, ConstraintException {

        List<ColumnMetadata> columnsMetadata = description.getColumnMetadata();
        Map<String, Value> map = new HashMap<>();
        for (int i = 0; i < columnsMetadata.size(); i++) {
            ColumnMetadata columnMetadata = columnsMetadata.get(i);

            Object obj = null;
            if (values.size() > i) {
                obj = values.get(i);
            }
            if (obj == null && columnMetadata.isNotNull()) {
                throw new ConstraintException(this.createColumnReference(columnMetadata));
            }
            if (obj != null && !columnMetadata.getJavaClass().isInstance(obj)) {
                throw new WrongValueTypeException(this.createColumnReference(columnMetadata),
                        columnMetadata.getJavaClass(), obj.getClass());
            }
            Value value = new Value(columnMetadata.getJavaClass(), obj);
            map.put(columnMetadata.getColumnName(), value);
        }
        rows.add(new Row(map));
    }

    @Override
    public void insert(List<String> columns, List<Object> values) throws WrongValueTypeException, ConstraintException {

        if (columns.size() != values.size()) {
//            throw new Exception("");
        }

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
