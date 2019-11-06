package api;

import api.selectionPredicate.Predicate;

public interface JoinTableReference extends TableReference {

    enum JoinType {
        INNER_JOIN,
        LEFT_OUTER_JOIN,
        RIGHT_OUTER_JOIN
    }

    TableReference getLeftTableReference();

    TableReference getRightTableReference();

    Predicate getSelectionPredicate();

    JoinType getJoinType();
}
