package clientImpl.assignment;

import org.jetbrains.annotations.NotNull;
import sqlapi.columnExpr.ColumnExpression;
import sqlapi.misc.AssignmentOperation;

public class AssignmentOperationFactory {

    private AssignmentOperationFactory() {
    }

    public static @NotNull AssignmentOperation assign(String columnName, ColumnExpression columnExpression) {
        return new AssignmentOperationImpl(columnName, columnExpression);
    }
}
