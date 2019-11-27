package clientImpl.predicates;

import org.jetbrains.annotations.NotNull;
import sqlapi.columnExpr.ColumnExpression;
import sqlapi.predicates.GreaterThanOrEqualsPredicate;

final class GreaterThanOrEqualsPredicateImpl extends BinaryPredicateImpl implements GreaterThanOrEqualsPredicate {

    GreaterThanOrEqualsPredicateImpl(@NotNull ColumnExpression leftOperand, @NotNull ColumnExpression rightOperand) {
        super(leftOperand, rightOperand);
    }

    @NotNull
    @Override
    protected String getOperatorString() {
        return ">=";
    }
}
