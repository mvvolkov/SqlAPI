package api.tables;

import api.predicates.Predicate;

public interface JoinedTableReference extends TableReference {

    TableReference getLeftTableReference();

    TableReference getRightTableReference();

    Predicate getPredicate();
}
