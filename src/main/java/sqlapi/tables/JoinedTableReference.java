package sqlapi.tables;

import org.jetbrains.annotations.NotNull;
import sqlapi.predicates.Predicate;

public interface JoinedTableReference extends TableReference {

    @NotNull TableReference getLeftTableReference();

    @NotNull TableReference getRightTableReference();

    @NotNull Predicate getPredicate();
}
