package SimpleFileImpl;

import sqlapi.*;
import sqlapi.SelectionCriteria;

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
        return null;
    }

    @Override
    public void insert(List<Object> values) throws Exception {

        List<ColumnMetadata> columnsMetadata = description.getColumnMetadata();
        if (columnsMetadata.size() != values.size()) {
            throw new Exception("");
        }

        Map<String, Value> map = new HashMap<>();

        for (int i = 0; i < columnsMetadata.size(); i++) {
            ColumnMetadata columnMetadata = columnsMetadata.get(i);
            Object obj = values.get(i);
            Value value;
            switch (columnMetadata.getTypeName()) {
                case "INTEGER":
                    value = new IntegerValue(obj);
                    break;
                case "VARCHAR":
                    value = new VarcharValue(obj);
                    break;
                default:
                    throw new Exception("");
            }
            map.put(columnMetadata.getColumnName(), value);
        }
        rows.add(new Row(map));
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
