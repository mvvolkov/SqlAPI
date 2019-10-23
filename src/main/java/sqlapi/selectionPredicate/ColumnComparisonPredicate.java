package sqlapi.selectionPredicate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sqlapi.ColumnReference;

public final class ColumnComparisonPredicate extends AbstractPredicate {

    @NotNull
    private final ColumnReference columnReference;

    @Nullable
    private final Object value;


    public ColumnComparisonPredicate(@NotNull Type type, @NotNull ColumnReference columnReference,
                                     @Nullable Object value) {
        super(type);
        this.columnReference = columnReference;
        this.value = value;
    }

    @NotNull
    public ColumnReference getColumnReference() {
        return columnReference;
    }


    @Nullable
    public Object getValue() {
        return value;
    }


    @Override
    public String toString() {
        return columnReference + " " + this.getOperatorString() + " " + getSqlString(value);
    }
}
