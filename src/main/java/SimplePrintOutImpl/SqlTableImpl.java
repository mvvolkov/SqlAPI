package SimplePrintOutImpl;

import api.*;

import java.util.List;
import java.util.stream.Collectors;

public class SqlTableImpl implements Table {


    private final Database database;


    private final TableDescription description;

    public SqlTableImpl(Database database, TableDescription description) {
        this.database = database;
        this.description = description;
    }


    @Override
    public TableDescription getDescription() {
        return description;
    }

    private <T> String getStringFromNewValue(SqlInsertableValue<? extends T> value) {
        if (value instanceof SqlInsertableValue.StringValue) {
            return '\'' + String.valueOf(value.getValue()) + '\'';
        } else if (value instanceof SqlInsertableValue.NullValue) {
            return "NULL";
        }
        return String.valueOf(value.getValue());
    }


    @Override
    public void insert(List<SqlInsertableValue<?>> values) {
        StringBuilder sb = new StringBuilder("INSERT INTO ");
        sb.append(description.getName());
        sb.append(" VALUES (");
        String valuesString = values.stream().map(this::getStringFromNewValue).collect(Collectors.joining(", "));
        sb.append(valuesString);
        sb.append(");");
        System.out.println(sb);
    }

    @Override
    public void insert(List<String> columns, List<SqlInsertableValue<?>> values) {
        StringBuilder sb = new StringBuilder("INSERT INTO ");
        sb.append(description.getName());
        sb.append(" (");
        sb.append(columns.stream().collect(Collectors.joining(", ")));
        sb.append(") VALUES (");
        String valuesString = values.stream().map(this::getStringFromNewValue).collect(Collectors.joining(", "));
        sb.append(valuesString);
        sb.append(");");
        System.out.println(sb);
    }

    @Override
    public void insert(SqlSelectionResult rows) {

    }

    private String getConditionString(SqlSelectionCondition condition) {
        return null;
    }

    @Override
    public void delete(SqlSelectionCondition selectionCondition) {
        StringBuilder sb = new StringBuilder("DELETE FROM ");
        sb.append(description.getName());
        if (selectionCondition.isNotEmpty()) {
            sb.append(" WHERE ");
            sb.append(this.getConditionString(selectionCondition));
        }
        System.out.println(sb);
    }

    @Override
    public void update(List<SqlSelectionResultValue> newValues, SqlSelectionCondition selectionCondition) {
    }

    @Override
    public List<SqlSelectionResultRow> select(List<SelectionUnit> selectionUnits, SqlSelectionCondition selectionCondition) {
        return null;
    }
}
