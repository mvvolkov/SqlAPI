package clientImpl.predicates;

import api.columnExpr.ColumnRef;
import api.columnExpr.ColumnValue;
import api.predicates.ColumnInPredicate;
import api.predicates.ColumnIsNullPredicate;
import org.jetbrains.annotations.NotNull;

import java.util.List;

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
