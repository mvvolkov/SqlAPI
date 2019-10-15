package SimpleFileImpl;

import api.*;

import java.util.List;

public class SqlTableImpl implements Table {

    private final Database database;

    private final TableDescription description;

    public SqlTableImpl(Database database, TableDescription description) {
        this.database = database;
        this.description = description;
    }


    @Override
    public TableDescription getDescription() {
        return null;
    }

    @Override
    public void insert(List<SqlInsertableValue<?>> values) {

    }

    @Override
    public void insert(List<String> columns, List<SqlInsertableValue<?>> values) {

    }

    @Override
    public void insert(SqlSelectionResult rows) {

    }

    @Override
    public void delete(SqlSelectionCondition selectionCondition) {

    }

    @Override
    public void update(List<SqlSelectionResultValue> newValues, SqlSelectionCondition selectionCondition) {

    }

    @Override
    public List<SqlSelectionResultRow> select(List<SelectionUnit> selectionUnits, SqlSelectionCondition selectionCondition) {
        return null;
    }
}
