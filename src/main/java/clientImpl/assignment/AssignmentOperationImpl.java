package clientImpl.assignment;

import org.jetbrains.annotations.NotNull;
import sqlapi.misc.AssignmentOperation;
import sqlapi.columnExpr.ColumnExpression;

final class AssignmentOperationImpl implements AssignmentOperation {

    @NotNull
    private final String columnName;

    @NotNull
    private final ColumnExpression value;

    AssignmentOperationImpl(@NotNull String columnName, @NotNull ColumnExpression value) {
        this.columnName = columnName;
        this.value = value;
    }

    @NotNull
    @Override
    public String getColumnName() {
        return columnName;
    }

    @NotNull
    @Override
    public ColumnExpression getValue() {
        return value;
    }

    @Override public String toString() {
        return columnName + " = " + value;
    }
}
