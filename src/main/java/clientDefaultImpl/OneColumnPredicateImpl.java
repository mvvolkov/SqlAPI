package clientDefaultImpl;

import api.selectionPredicate.OneColumnPredicate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import api.ColumnReference;

public final class OneColumnPredicateImpl extends SelectionPredicateImpl implements OneColumnPredicate {

    @NotNull
    private final ColumnReference columnReference;

    @Nullable
    private final Comparable value;


    public OneColumnPredicateImpl(@NotNull Type type, @NotNull ColumnReference columnReference,
                                  @Nullable Comparable value) {
        super(type);
        this.columnReference = columnReference;
        this.value = value;
    }

    @NotNull
    @Override
    public ColumnReference getColumnReference() {
        return columnReference;
    }


    @Nullable
    @Override
    public Comparable getValue() {
        return value;
    }


    @Override
    public String toString() {
        return columnReference + " " + this.getOperatorString() + " " + getSqlString(value);
    }
}
