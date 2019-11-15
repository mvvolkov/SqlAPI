package clientImpl.columnExpr;

import api.columnExpr.BinaryColumnExpression;
import api.columnExpr.ColumnExpression;
import org.jetbrains.annotations.NotNull;

public final class BinaryColumnExprImpl extends ColumnExprImpl
        implements BinaryColumnExpression {

    @NotNull
    private final ColumnExpression leftOperand;

    @NotNull
    private final ColumnExpression rightOperand;

    public BinaryColumnExprImpl(@NotNull ExprType exprType, @NotNull ColumnExpression leftOperand,
                                @NotNull ColumnExpression rightOperand, @NotNull String alias) {
        super(exprType, alias);
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }


    @NotNull
    @Override
    public ColumnExpression getLeftOperand() {
        return leftOperand;
    }

    @NotNull
    @Override
    public ColumnExpression getRightOperand() {
        return rightOperand;
    }

    @Override
    public String toString() {
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

