package sqlapi;

import sqlapi.selectionPredicate.SelectionPredicate;

public interface JoinTableReference extends TableReference {

    enum Type {
        INNER_JOIN,
        LEFT_OUTER_JOIN,
        RIGHT_OUTER_JOIN
    }

    TableReference getLeftTableReference();

    TableReference getRightTableReference();

    SelectionPredicate getSelectionPredicate();

    Type getType();
}
