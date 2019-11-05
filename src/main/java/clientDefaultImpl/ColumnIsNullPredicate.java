package clientDefaultImpl;

import org.jetbrains.annotations.NotNull;
import api.ColumnReference;

/**
 * The predicate for checking whether the column value is NULL or NOT NULL.
 */
public class ColumnIsNullPredicate extends SelectionPredicateImpl {


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
