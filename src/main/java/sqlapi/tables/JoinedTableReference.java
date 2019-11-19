package sqlapi.tables;

import sqlapi.predicates.Predicate;

public interface JoinedTableReference extends TableReference {

    TableReference getLeftTableReference();

    TableReference getRightTableReference();

    Predicate getPredicate();
}
