package api.selectionPredicate;

import api.ColumnReference;

public interface ColumnColumnPredicate extends Predicate {

    ColumnReference getLeftColumn();

    ColumnReference getRightColumn();
}
