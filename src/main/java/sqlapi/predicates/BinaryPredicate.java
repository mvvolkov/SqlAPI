package sqlapi.predicates;

import sqlapi.columnExpr.ColumnExpression;

public interface BinaryPredicate extends Predicate {

    ColumnExpression getLeftOperand();

    ColumnExpression getRightOperand();
}
