package sqlapi.selectionPredicate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sqlapi.ColumnReference;

public final class OneColumnPredicate extends SelectionPredicate {

    @NotNull
    private final ColumnReference columnReference;

    @Nullable
    private final Comparable value;


    public OneColumnPredicate(@NotNull Type type, @NotNull ColumnReference columnReference,
                              @Nullable Comparable value) {
        super(type);
        this.columnReference = columnReference;
        this.value = value;
    }

    @NotNull
    public ColumnReference getColumnReference() {
        return columnReference;
    }


    @Nullable
    public Comparable getValue() {
        return value;
    }


    @Override
    public String toString() {
        return columnReference + " " + this.getOperatorString() + " " + getSqlString(value);
    }
}
