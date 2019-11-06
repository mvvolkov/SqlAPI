package api.selectionPredicate;

import api.ColumnReference;

public interface ColumnNullPredicate extends Predicate {

    ColumnReference getColumnReference();

    default boolean isNull() {
        return this.getType() == Type.IS_NULL;
    }

    default boolean isNotNull() {
        return this.getType() == Type.IS_NOT_NULL;
    }
}
