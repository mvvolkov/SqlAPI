package sqlapi.selectionPredicate;

import org.jetbrains.annotations.NotNull;
import sqlapi.ColumnReference;

/**
 * The predicate for checking whether the column value is NULL or NOT NULL.
 */
public class ColumnIsNullPredicate extends SelectionPredicate {


    @NotNull
    private final ColumnReference columnReference;

    ColumnIsNullPredicate(Type type, ColumnReference columnReference) {
        super(type);
        this.columnReference = columnReference;
    }

    @NotNull
    public ColumnReference getColumnReference() {
        return columnReference;
    }

    @Override
    public String toString() {
        return columnReference + " " + this.getOperatorString();
    }
}
