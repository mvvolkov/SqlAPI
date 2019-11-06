package clientDefaultImpl;

import api.selectionPredicate.ColumnNullPredicate;
import org.jetbrains.annotations.NotNull;
import api.ColumnReference;

/**
 * The predicate for checking whether the column value is NULL or NOT NULL.
 */
public class ColumnNullPredicateImpl extends SelectionPredicateImpl implements ColumnNullPredicate {


    @NotNull
    private final ColumnReference columnReference;

    ColumnNullPredicateImpl(Type type, ColumnReference columnReference) {
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
