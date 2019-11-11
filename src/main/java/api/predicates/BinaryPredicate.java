package api.predicates;

import api.columnExpr.ColumnExpression;

public interface BinaryPredicate extends Predicate {

    ColumnExpression getLeftOperand();

    ColumnExpression getRightOperand();
}
