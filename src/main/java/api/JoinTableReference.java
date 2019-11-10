package api;

import api.predicates.Predicate;

public interface JoinTableReference extends TableReference {

    TableReference getLeftTableReference();

    TableReference getRightTableReference();

    Predicate getSelectionPredicate();
}
