package sqlapi.misc;

import org.jetbrains.annotations.NotNull;
import sqlapi.columnExpr.ColumnExpression;

public interface AssignmentOperation {

    @NotNull
    String getColumnName();

    @NotNull
    ColumnExpression getValue();
}
