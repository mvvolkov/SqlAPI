package clientImpl.queries;

import org.jetbrains.annotations.NotNull;
import sqlapi.columnExpr.InputValue;
import sqlapi.queries.InsertQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

final class InsertQueryImpl extends TableQueryImpl implements InsertQuery {

    @NotNull
    private final List<String> columns;

    @NotNull
    private final List<InputValue> inputValues;

    InsertQueryImpl(@NotNull String databaseName, @NotNull String tableName,
                    @NotNull List<String> columns,
                    @NotNull List<InputValue> inputValues) {
        super(databaseName, tableName);
        this.columns = columns;
        this.inputValues = inputValues;
    }


    @NotNull
    @Override
    public List<InputValue> getInputValues() {
        return inputValues;
    }

    @NotNull
    @Override
    public List<String> getColumns() {
        return columns;
    }

    @Override public void setParameters(Object... values) {
        List<Object> parameters = new ArrayList<>(Arrays.asList(values));
        for (InputValue inputValue : inputValues) {
            inputValue.setParameters(parameters);
        }

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
        String valuesString = inputValues.stream().map(InputValue::toString)
                .collect(Collectors.joining(", "));
        sb.append(valuesString);
        sb.append(");");
        return sb.toString();
    }

}
