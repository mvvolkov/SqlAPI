package clientImpl.columnExpr;

import org.jetbrains.annotations.NotNull;
import sqlapi.columnExpr.ColumnExpression;
import sqlapi.columnExpr.SumColumnExpression;

final class SumColumnExpressionImpl extends BinaryColumnExprImpl implements SumColumnExpression {

    SumColumnExpressionImpl(@NotNull ColumnExpression leftOperand, @NotNull ColumnExpression rightOperand, @NotNull String alias) {
        super(leftOperand, rightOperand, alias);
    }

    @Override
    protected String getOperatorString() {
        return "+";
    }
}
