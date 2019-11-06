package clientDefaultImpl;

import api.selectionPredicate.ColumnNullPredicate;
import org.jetbrains.annotations.NotNull;

/**
 * The predicate for checking whether the column value is NULL or NOT NULL.
 */
public class ColumnNullPredicateImpl extends SelectionPredicateImpl implements ColumnNullPredicate {


    @NotNull
    private final ColumnReferenceImpl columnReference;

    ColumnNullPredicateImpl(Type type, ColumnReferenceImpl columnReference) {
        super(type);
        this.columnReference = columnReference;
    }

    @NotNull
    public ColumnReferenceImpl getColumnReference() {
        return columnReference;
    }

    @Override
    public String toString() {
        return columnReference + " " + this.getOperatorString();
    }
}
