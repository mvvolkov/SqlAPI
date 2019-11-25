package clientImpl.columnExpr;

import sqlapi.columnExpr.BinaryColumnExpression;
import sqlapi.columnExpr.ColumnExpression;
import org.jetbrains.annotations.NotNull;

final class BinaryColumnExprImpl extends ColumnExprImpl
        implements BinaryColumnExpression {

    @NotNull
    private final ColumnExpression leftOperand;

    @NotNull
    private final ColumnExpression rightOperand;

    BinaryColumnExprImpl(@NotNull ExprType exprType,
                         @NotNull ColumnExpression leftOperand,
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
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append(leftOperand);
        sb.append(" ");
        sb.append(getOperatorString());
        sb.append(" ");
        sb.append(rightOperand);
        sb.append(")");
        if (!alias.isEmpty()) {
            sb.append(" AS ");
            sb.append(alias);
        }
        return sb.toString();
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

