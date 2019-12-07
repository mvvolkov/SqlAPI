package clientImpl.predicates;

import org.jetbrains.annotations.NotNull;
import sqlapi.columnExpr.ColumnExpression;
import sqlapi.predicates.binaryPredicate.EqualsPredicate;

final class EqualsPredicateImpl extends BinaryPredicateImpl implements EqualsPredicate {

    EqualsPredicateImpl(@NotNull ColumnExpression leftOperand, @NotNull ColumnExpression rightOperand) {
        super(leftOperand, rightOperand);
    }

    @NotNull
    @Override
    protected String getOperatorString() {
        return "=";
    }
}
