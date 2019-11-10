package clientImpl.predicates;

import api.columnExpr.ColumnRef;
import api.columnExpr.ColumnValue;
import api.predicates.ColumnValuePredicate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ColumnValuePredicateImpl extends SelectionPredicateImpl
        implements ColumnValuePredicate {

    @NotNull
    private final ColumnRef columnReference;

    @Nullable
    private final ColumnValue value;


    public ColumnValuePredicateImpl(@NotNull Type type,
                                    @NotNull ColumnRef columnReference,
                                    @Nullable ColumnValue value) {
        super(type);
        this.columnReference = columnReference;
        this.value = value;
    }

    @NotNull
    @Override
    public ColumnRef getColumnRef() {
        return columnReference;
    }


    @Nullable
    @Override
    public ColumnValue getColumnValue() {
        return value;
    }


    @Override
    public String toString() {
        return columnReference + " " + this.getOperatorString() + " " +
                getSqlString(value);
    }
}
