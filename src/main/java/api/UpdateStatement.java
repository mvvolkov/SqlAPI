package api;

import api.selectionPredicate.Predicate;

import java.util.List;

public interface UpdateStatement extends SqlStatement {

    List<AssignmentOperation> getAssignmentOperations();

    Predicate getPredicate();

    @Override
    default Type getType() {
        return Type.UPDATE;
    }
}
