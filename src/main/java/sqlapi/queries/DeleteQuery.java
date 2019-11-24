package sqlapi.queries;

import org.jetbrains.annotations.NotNull;
import sqlapi.predicates.Predicate;

public interface DeleteQuery extends SqlTableQuery {

    @NotNull Predicate getPredicate();
}
