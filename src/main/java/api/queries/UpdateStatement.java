package api.queries;

import api.AssignmentOperation;
import api.predicates.Predicate;

import java.util.List;

public interface UpdateStatement extends SqlStatement {

    List<AssignmentOperation> getAssignmentOperations();

    Predicate getPredicate();

    @Override
    default Type getType() {
        return Type.UPDATE;
    }
}
