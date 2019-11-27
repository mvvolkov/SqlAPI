package clientImpl.columnExpr;

import org.jetbrains.annotations.NotNull;
import sqlapi.columnExpr.ColumnExpression;
import sqlapi.columnExpr.DiffColumnExpression;

final class DiffColumnExpressionImpl extends BinaryColumnExprImpl implements DiffColumnExpression {

    DiffColumnExpressionImpl(@NotNull ColumnExpression leftOperand, @NotNull ColumnExpression rightOperand, @NotNull String alias) {
        super(leftOperand, rightOperand, alias);
    }

    @Override
    protected String getOperatorString() {
        return "-";
    }
}
