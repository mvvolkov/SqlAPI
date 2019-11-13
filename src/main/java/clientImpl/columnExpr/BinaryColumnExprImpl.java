package clientImpl.columnExpr;

import api.columnExpr.BinaryColumnExpression;
import api.columnExpr.ColumnExpression;

public final class BinaryColumnExprImpl extends ColumnExprImpl
        implements BinaryColumnExpression {

    private final ColumnExpression leftOperand;

    private final ColumnExpression rightOperand;

    public BinaryColumnExprImpl(ExprType exprType, ColumnExpression leftOperand,
                                ColumnExpression rightOperand, String alias) {
        super(exprType, alias);
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }


    @Override public ColumnExpression getLeftOperand() {
        return leftOperand;
    }

    @Override public ColumnExpression getRightOperand() {
        return rightOperand;
    }

    @Override public String toString() {
        return "(" + leftOperand + " " + getOperatorString() + " " + rightOperand + ")";
    }

    private String getOperatorString() {
        switch (exprType) {
            case SUM:
                return "+";
            case DIFF:
                return "-";
            case PRODUCT:
                return "*";
            case DIVIDE:
                return "/";
            default:
                return "";
        }
    }
}

