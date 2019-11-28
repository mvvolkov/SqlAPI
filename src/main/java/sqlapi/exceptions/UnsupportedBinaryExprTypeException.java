package sqlapi.exceptions;

import sqlapi.columnExpr.BinaryColumnExpression;

public class UnsupportedBinaryExprTypeException extends SqlException {

    private final BinaryColumnExpression expr;

    public UnsupportedBinaryExprTypeException(BinaryColumnExpression expr) {
        this.expr = expr;
    }

    public BinaryColumnExpression getExpression() {
        return expr;
    }

    @Override
    public String getMessage() {
        return "Unsupported binary expression type: " + expr;
    }
}
