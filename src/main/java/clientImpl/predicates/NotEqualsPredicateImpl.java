package clientImpl.predicates;

import org.jetbrains.annotations.NotNull;
import sqlapi.columnExpr.ColumnExpression;
import sqlapi.predicates.NotEqualsPredicate;

final class NotEqualsPredicateImpl extends BinaryPredicateImpl implements NotEqualsPredicate {

    NotEqualsPredicateImpl(@NotNull ColumnExpression leftOperand, @NotNull ColumnExpression rightOperand) {
        super(leftOperand, rightOperand);
    }

    @NotNull
    @Override
    protected String getOperatorString() {
        return "!=";
    }
}
