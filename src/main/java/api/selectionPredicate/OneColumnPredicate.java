package api.selectionPredicate;

import api.ColumnReference;

public interface OneColumnPredicate extends Predicate {

    ColumnReference getColumnReference();

    Comparable getValue();
}
