package clientImpl.columnExpr;

import org.jetbrains.annotations.NotNull;
import sqlapi.columnExpr.ColumnExpression;
import sqlapi.columnExpr.binaryExpr.DivisionColumnExpression;

final class DivisionColumnExpressionImpl extends BinaryColumnExprImpl implements DivisionColumnExpression {

    DivisionColumnExpressionImpl(@NotNull ColumnExpression leftOperand, @NotNull ColumnExpression rightOperand, @NotNull String alias) {
        super(leftOperand, rightOperand, alias);
    }

    @Override
    protected String getOperatorString() {
        return "/";
    }
}
