package clientImpl.queries;

import org.jetbrains.annotations.NotNull;
import sqlapi.misc.AssignmentOperation;
import sqlapi.predicates.Predicate;
import sqlapi.queries.UpdateQuery;

import java.util.Collection;
import java.util.stream.Collectors;

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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("UPDATE ");
        sb.append(this.getDatabaseName());
        sb.append(".");
        sb.append(this.getTableName());
        sb.append(" SET ");
        sb.append(
                assignmentOperations.stream().map(AssignmentOperation::toString).collect(
                        Collectors.joining(", ")));
        if (!predicate.isEmpty()) {
            sb.append(" WHERE ").append(predicate);
        }
        sb.append(";");
        return sb.toString();
    }
}
