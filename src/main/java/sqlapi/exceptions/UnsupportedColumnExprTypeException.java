package sqlapi.exceptions;

import org.jetbrains.annotations.NotNull;
import sqlapi.columnExpr.ColumnExpression;

public final class UnsupportedColumnExprTypeException extends SqlException {

    @NotNull
    private final ColumnExpression expr;

    public UnsupportedColumnExprTypeException(@NotNull ColumnExpression expr) {
        this.expr = expr;
    }

    @NotNull
    public ColumnExpression getExpression() {
        return expr;
    }

    @Override
    public String getMessage() {
        return "Unsupported column expression type: " + expr;
    }
}
