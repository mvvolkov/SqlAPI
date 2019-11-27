package clientImpl.predicates;

import org.jetbrains.annotations.NotNull;
import sqlapi.columnExpr.ColumnExpression;
import sqlapi.predicates.GreaterThanPredicate;

final class GreaterThanPredicateImpl extends BinaryPredicateImpl implements GreaterThanPredicate {

    GreaterThanPredicateImpl(@NotNull ColumnExpression leftOperand, @NotNull ColumnExpression rightOperand) {
        super(leftOperand, rightOperand);
    }

    @NotNull
    @Override
    protected String getOperatorString() {
        return ">";
    }
}
