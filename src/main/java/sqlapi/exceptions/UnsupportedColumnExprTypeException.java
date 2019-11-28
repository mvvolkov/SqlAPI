package sqlapi.exceptions;

import sqlapi.columnExpr.ColumnExpression;

public class UnsupportedColumnExprTypeException extends SqlException {

    private final ColumnExpression expr;

    public UnsupportedColumnExprTypeException(ColumnExpression expr) {
        this.expr = expr;
    }

    public ColumnExpression getExpression() {
        return expr;
    }

    @Override
    public String getMessage() {
        return "Unsupported column expression type: " + expr;
    }
}
