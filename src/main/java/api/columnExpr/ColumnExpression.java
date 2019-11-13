package api.columnExpr;

public interface ColumnExpression {

    enum ExprType {
        COLUMN_VALUE,
        COLUMN_REF,
        NULL_VALUE,
        SUM,
        DIFF,
        PRODUCT,
        DIVIDE
    }

    ExprType getExprType();
}
