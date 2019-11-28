package clientImpl.queries;

import org.jetbrains.annotations.NotNull;
import sqlapi.columnExpr.ColumnValue;
import sqlapi.queries.InsertQuery;

import java.util.List;
import java.util.stream.Collectors;

final class InsertQueryImpl extends TableActionQueryImpl implements InsertQuery {

    @NotNull
    private final List<String> columns;

    @NotNull
    private final List<ColumnValue> values;

    InsertQueryImpl(@NotNull String databaseName, @NotNull String tableName,
                    @NotNull List<String> columns, @NotNull List<ColumnValue> values) {
        super(databaseName, tableName);
        this.columns = columns;
        this.values = values;
    }


    @NotNull
    @Override
    public List<ColumnValue> getValues() {
        return values;
    }

    @NotNull
    @Override
    public List<String> getColumns() {
        return columns;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("INSERT INTO ");
        sb.append(this.getDatabaseName());
        sb.append(".");
        sb.append(this.getTableName());
        if (!this.getColumns().isEmpty()) {
            sb.append("(");
            sb.append(String.join(", ", this.getColumns()));
            sb.append(")");
        }
        sb.append(" VALUES (");
        String valuesString = values.stream().map(this::getStringFromValue)
                .collect(Collectors.joining(", "));
        sb.append(valuesString);
        sb.append(");");
        return sb.toString();
    }

    private String getStringFromValue(ColumnValue columnValue) {
        Object value = columnValue.getValue();
        if (value instanceof String) {
            return '\'' + (String) value + '\'';
        }
        if (value == null) {
            return "NULL";
        }
        return String.valueOf(value);
    }
}