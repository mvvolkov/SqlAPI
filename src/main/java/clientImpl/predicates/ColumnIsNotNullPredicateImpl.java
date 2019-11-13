package clientImpl.predicates;

import api.columnExpr.ColumnRef;
import api.predicates.ColumnIsNotNullPredicate;
import api.predicates.ColumnIsNullPredicate;
import org.jetbrains.annotations.NotNull;

public final class ColumnIsNotNullPredicateImpl extends PredicateImpl
        implements ColumnIsNotNullPredicate {

    @NotNull
    private final ColumnRef columnRef;


    public ColumnIsNotNullPredicateImpl(@NotNull ColumnRef columnRef) {
        super(Type.IS_NOT_NULL);
        this.columnRef = columnRef;
    }


    @Override
    public ColumnRef getColumnRef() {
        return columnRef;
    }

    @Override
    public String toString() {
        return columnRef + " IS NOT NULL";
    }
}
