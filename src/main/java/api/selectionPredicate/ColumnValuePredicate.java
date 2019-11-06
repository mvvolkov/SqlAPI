package api.selectionPredicate;

import api.ColumnReference;

public interface ColumnValuePredicate extends Predicate {

    ColumnReference getColumnReference();

    Comparable getValue();
}
