package clientImpl.columnExpr;

import api.columnExpr.BinaryColumnExpression;
import api.columnExpr.ColumnExpression;

public class BinaryColumnExprImpl extends ColumnExprImpl
        implements BinaryColumnExpression {

    private final ColumnExpression leftOperand;

    private final ColumnExpression rightOperand;

    public BinaryColumnExprImpl(ExprType exprType, ColumnExpression leftOperand,
                                ColumnExpression rightOperand) {
        super(exprType);
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }


    @Override public ColumnExpression getLeftOperand() {
        return leftOperand;
    }

    @Override public ColumnExpression getRightOperand() {
        return rightOperand;
    }
}

