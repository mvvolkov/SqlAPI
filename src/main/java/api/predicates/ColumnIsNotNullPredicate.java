package api.predicates;

import api.columnExpr.ColumnRef;

public interface ColumnIsNotNullPredicate extends Predicate {

    ColumnRef getColumnRef();

    @Override
    default Type getType() {
        return Type.IS_NOT_NULL;
    }
}
