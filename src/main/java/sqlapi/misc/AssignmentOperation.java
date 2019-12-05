package sqlapi.misc;

import org.jetbrains.annotations.NotNull;
import sqlapi.columnExpr.ColumnExpression;

import java.util.List;

public interface AssignmentOperation {

    @NotNull
    String getColumnName();

    @NotNull
    ColumnExpression getValue();

    default void setParameters(List<Object> parameters) {
        getValue().setParameters(parameters);
    }
}
