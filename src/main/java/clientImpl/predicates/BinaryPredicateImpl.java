package clientImpl.predicates;

import sqlapi.columnExpr.ColumnExpression;
import sqlapi.predicates.binaryPredicate.BinaryPredicate;
import org.jetbrains.annotations.NotNull;

abstract class BinaryPredicateImpl extends PredicateImpl implements BinaryPredicate {

    @NotNull
    private final ColumnExpression leftOperand;

    @NotNull
    private final ColumnExpression rightOperand;

    BinaryPredicateImpl(@NotNull ColumnExpression leftOperand,
                        @NotNull ColumnExpression rightOperand) {
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
        return leftOperand + " " + this.getOperatorString() + " " + rightOperand;
    }

    protected abstract @NotNull String getOperatorString();
}
