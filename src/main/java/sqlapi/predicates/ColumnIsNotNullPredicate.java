package sqlapi.predicates;

import sqlapi.columnExpr.ColumnRef;

public interface ColumnIsNotNullPredicate extends Predicate {

    ColumnRef getColumnRef();

    @Override
    default Type getType() {
        return Type.IS_NOT_NULL;
    }
}
