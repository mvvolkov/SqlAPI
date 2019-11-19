package sqlapi.predicates;

import sqlapi.columnExpr.ColumnRef;

public interface ColumnIsNullPredicate extends Predicate {

    ColumnRef getColumnRef();

    @Override
    default Type getType() {
        return Type.IS_NULL;
    }
}
