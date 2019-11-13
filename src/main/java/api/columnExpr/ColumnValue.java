package api.columnExpr;

public interface ColumnValue extends ColumnExpression {

    Object getValue();

    @Override
    default ExprType getExprType() {
        return ExprType.COLUMN_VALUE;
    }
}
