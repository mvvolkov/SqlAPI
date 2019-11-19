package clientImpl.predicates;

import sqlapi.columnExpr.ColumnRef;
import sqlapi.predicates.ColumnIsNullPredicate;
import org.jetbrains.annotations.NotNull;

public final class ColumnIsNullPredicateImpl extends PredicateImpl
        implements ColumnIsNullPredicate {

    @NotNull
    private final ColumnRef columnRef;


    public ColumnIsNullPredicateImpl(@NotNull ColumnRef columnRef) {
        super(Type.IS_NULL);
        this.columnRef = columnRef;
    }


    @Override
    public ColumnRef getColumnRef() {
        return columnRef;
    }

    @Override
    public String toString() {
        return columnRef + " IS NULL";
    }
}
