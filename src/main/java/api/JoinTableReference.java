package api;

import api.selectionPredicate.Predicate;

public interface JoinTableReference extends TableReference {

    TableReference getLeftTableReference();

    TableReference getRightTableReference();

    Predicate getSelectionPredicate();
}
