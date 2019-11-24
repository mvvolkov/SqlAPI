package clientImpl.queries;

import org.jetbrains.annotations.NotNull;
import sqlapi.misc.AssignmentOperation;
import sqlapi.queries.UpdateQuery;
import sqlapi.predicates.Predicate;

import java.util.Collection;
import java.util.List;

final class UpdateQueryImpl extends AbstractSqlTableQueryImpl
        implements UpdateQuery {

    private final @NotNull Collection<AssignmentOperation> assignmentOperations;
    private final @NotNull Predicate predicate;

    UpdateQueryImpl(@NotNull String databaseName, @NotNull String tableName,
                    @NotNull Collection<AssignmentOperation> assignmentOperations,
                    @NotNull Predicate predicate) {
        super(databaseName, tableName);
        this.assignmentOperations = assignmentOperations;
        this.predicate = predicate;
    }

    @NotNull @Override
    public Collection<AssignmentOperation> getAssignmentOperations() {
        return assignmentOperations;
    }

    @NotNull @Override
    public Predicate getPredicate() {
        return predicate;
    }
}
