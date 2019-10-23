package sqlapi.selectionPredicate;

import org.jetbrains.annotations.NotNull;
import sqlapi.ColumnReference;

public final class BinaryPredicate extends AbstractPredicate {


    @NotNull
    private final ColumnReference columnReference;

    private final Object value;


    BinaryPredicate(@NotNull Type type, @NotNull ColumnReference columnReference, Object value) {
        super(type);
        this.columnReference = columnReference;
        this.value = value;
    }

    @NotNull
    public ColumnReference getColumnReference() {
        return columnReference;
    }


    public Object getValue() {
        return value;
    }


    @Override
    public String toString() {
        return columnReference + " " + this.getOperatorString() + " " + getSqlString(value);
    }
}
