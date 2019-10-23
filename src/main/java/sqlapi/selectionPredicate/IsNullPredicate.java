package sqlapi.selectionPredicate;

import sqlapi.ColumnReference;

/**
 * The predicate for checking whether the column value is NULL or NOT NULL.
 */
public class IsNullPredicate extends AbstractPredicate {

    private final ColumnReference columnReference;

    IsNullPredicate(Type type, ColumnReference columnReference) {
        super(type);
        this.columnReference = columnReference;
    }

    @Override
    public String toString() {
        return columnReference + " " + this.getOperatorString();
    }
}
