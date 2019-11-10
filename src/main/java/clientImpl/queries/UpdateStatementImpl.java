package clientImpl.queries;

import api.AssignmentOperation;
import api.queries.UpdateStatement;
import api.predicates.Predicate;

import java.util.List;

public class UpdateStatementImpl extends AbstractSqlStatement implements UpdateStatement {

    private final List<AssignmentOperation> assignmentOperations;
    private final Predicate predicate;

    public UpdateStatementImpl(String databaseName, String tableName,
                               List<AssignmentOperation> assignmentOperations,
                               Predicate predicate) {
        super(databaseName, tableName);
        this.assignmentOperations = assignmentOperations;
        this.predicate = predicate;
    }

    @Override
    public List<AssignmentOperation> getAssignmentOperations() {
        return assignmentOperations;
    }

    @Override
    public Predicate getPredicate() {
        return predicate;
    }
}
