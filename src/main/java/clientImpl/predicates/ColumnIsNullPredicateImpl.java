package clientImpl.predicates;

import sqlapi.columnExpr.ColumnRef;
import sqlapi.predicates.ColumnIsNullPredicate;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;

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

    @Override
    public void setParameters(ArrayDeque<Object> parameters) {

    }
}
