package sqlapi;

import sqlapi.selectionPredicate.SelectionPredicate;

public interface JoinTableReference extends TableReference {

    TableReference getLeftTableReference();

    TableReference getRightTableReference();

    SelectionPredicate getSelectionPredicate();
}
