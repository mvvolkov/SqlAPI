package clientImpl.predicates;

import sqlapi.columnExpr.ColumnRef;
import sqlapi.predicates.ColumnIsNotNullPredicate;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;

final class ColumnIsNotNullPredicateImpl extends PredicateImpl
        implements ColumnIsNotNullPredicate {

    @NotNull
    private final ColumnRef columnRef;


    ColumnIsNotNullPredicateImpl(@NotNull ColumnRef columnRef) {
        this.columnRef = columnRef;
    }


    @NotNull
    @Override
    public ColumnRef getColumnRef() {
        return columnRef;
    }

    @Override
    public String toString() {
        return columnRef + " IS NOT NULL";
    }

}
