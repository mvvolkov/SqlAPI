package clientImpl.predicates;

import org.jetbrains.annotations.NotNull;
import sqlapi.columnExpr.ColumnExpression;
import sqlapi.predicates.LessThanPredicate;

final class LessThanPredicateImpl extends BinaryPredicateImpl implements LessThanPredicate {

    LessThanPredicateImpl(@NotNull ColumnExpression leftOperand, @NotNull ColumnExpression rightOperand) {
        super(leftOperand, rightOperand);
    }

    @NotNull
    @Override
    protected String getOperatorString() {
        return "<";
    }
}
