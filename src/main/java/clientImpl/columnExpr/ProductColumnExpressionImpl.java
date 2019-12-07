package clientImpl.columnExpr;

import org.jetbrains.annotations.NotNull;
import sqlapi.columnExpr.ColumnExpression;
import sqlapi.columnExpr.binaryExpr.ProductColumnExpression;

final class ProductColumnExpressionImpl extends BinaryColumnExprImpl implements ProductColumnExpression {

    ProductColumnExpressionImpl(@NotNull ColumnExpression leftOperand, @NotNull ColumnExpression rightOperand, @NotNull String alias) {
        super(leftOperand, rightOperand, alias);
    }

    @Override
    protected String getOperatorString() {
        return "*";
    }
}
