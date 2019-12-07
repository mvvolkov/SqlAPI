package clientImpl.predicates;

import sqlapi.columnExpr.ColumnRef;
import sqlapi.predicates.ColumnIsNullPredicate;
import org.jetbrains.annotations.NotNull;

final class ColumnIsNullPredicateImpl extends PredicateImpl
        implements ColumnIsNullPredicate {

    @NotNull
    private final ColumnRef columnRef;


    ColumnIsNullPredicateImpl(@NotNull ColumnRef columnRef) {
        this.columnRef = columnRef;
    }


    @NotNull
    @Override
    public ColumnRef getColumnRef() {
        return columnRef;
    }

    @Override
    public String toString() {
        return columnRef + " IS NULL";
    }


}
