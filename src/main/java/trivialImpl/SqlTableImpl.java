package trivialImpl;

import api.*;

import java.util.List;
import java.util.stream.Collectors;

public class SqlTableImpl implements SqlTable {


    private final Database database;

    private final SqlTableDescription description;

    public SqlTableImpl(Database database, SqlTableDescription description) {
        this.database = database;
        this.description = description;
    }

    @Override
    public String getName() {
        return description.getName();
    }

    @Override
    public void insert(List<SelectionResultRow> rows) {
        StringBuilder sb = new StringBuilder("INSERT INTO ");
        sb.append(description.getName());


        System.out.println(sb);
    }

    @Override
    public void insert(InsertData insertData) {

        StringBuilder sb = new StringBuilder("INSERT INTO ");
        sb.append(description.getName());
        if (insertData.hasColumnsList())
        {
            sb.append("(");
            String columns = insertData.getColumns().stream().collect(Collectors.joining(", "));
            sb.append(columns);
            sb.append(")");
        }
        sb.append(" VALUES (");
        String values = insertData.getValues().stream().map(obj -> String.valueOf(obj)).collect(Collectors.joining(", "));
        sb.append(values);
        sb.append(");");

        System.out.println(sb);
    }

    @Override
    public void delete(SelectionCondition selectionCondition) {
    }

    @Override
    public void update(List<SqlValue> newValues, SelectionCondition selectionCondition) {
    }

    @Override
    public List<SelectionResultRow> select(List<SelectionUnit> selectionUnits, SelectionCondition selectionCondition) {
        return null;
    }
}
