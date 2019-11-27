package clientImpl.predicates;

import org.jetbrains.annotations.NotNull;
import sqlapi.columnExpr.ColumnExpression;
import sqlapi.predicates.LessThanOrEqualsPredicate;

final class LessThanOrEqualsPredicateImpl extends BinaryPredicateImpl implements LessThanOrEqualsPredicate {

    LessThanOrEqualsPredicateImpl(@NotNull ColumnExpression leftOperand, @NotNull ColumnExpression rightOperand) {
        super(leftOperand, rightOperand);
    }

    @NotNull
    @Override
    protected String getOperatorString() {
        return "<=";
    }
}
