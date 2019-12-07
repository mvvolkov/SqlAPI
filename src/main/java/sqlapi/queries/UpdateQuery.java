package sqlapi.queries;

import org.jetbrains.annotations.NotNull;
import sqlapi.misc.AssignmentOperation;
import sqlapi.predicates.Predicate;

import java.util.Collection;

public interface UpdateQuery extends TableQuery {

    @NotNull Collection<AssignmentOperation> getAssignmentOperations();

    @NotNull Predicate getPredicate();
}
