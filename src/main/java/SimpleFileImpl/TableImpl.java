package SimpleFileImpl;

import sqlapi.*;
import sqlapi.SelectionCriteria;

import java.util.List;

public class TableImpl implements Table {

    private final Database database;

    private final TableMetadata description;

    public TableImpl(Database database, TableMetadata description) {
        this.database = database;
        this.description = description;
    }


    @Override
    public TableMetadata getMetadata() {
        return null;
    }

    @Override
    public void insert(List<Object> values) {

    }

    @Override
    public void insert(List<String> columns, List<Object> values) {

    }

    @Override
    public void insert(SelectionResultSet rows) {

    }

    @Override
    public void delete(SelectionCriteria selectionCriteria) {

    }

    @Override
    public void update(List<AssignmentOperation> newValues, SelectionCriteria selectionCriteria) {

    }

    @Override
    public List<SelectionResultRow> select(List<SelectionUnit> selectionUnits, SelectionCriteria selectionCriteria) {
        return null;
    }
}
