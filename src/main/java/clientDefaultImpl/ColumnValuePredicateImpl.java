package clientDefaultImpl;

import api.selectionPredicate.ColumnValuePredicate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ColumnValuePredicateImpl extends SelectionPredicateImpl implements ColumnValuePredicate {

    @NotNull
    private final ColumnReferenceImpl columnReference;

    @Nullable
    private final Comparable value;


    public ColumnValuePredicateImpl(@NotNull Type type, @NotNull ColumnReferenceImpl columnReference,
                                    @Nullable Comparable value) {
        super(type);
        this.columnReference = columnReference;
        this.value = value;
    }

    @NotNull
    @Override
    public ColumnReferenceImpl getColumnReference() {
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
