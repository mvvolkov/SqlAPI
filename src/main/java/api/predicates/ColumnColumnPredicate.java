package api.predicates;

import api.columnExpr.ColumnRef;

public interface ColumnColumnPredicate extends Predicate {

    ColumnRef getLeftColumnRef();

    ColumnRef getRightColumnRef();
}
