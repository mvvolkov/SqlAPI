package sqlapi.tables;

import org.jetbrains.annotations.NotNull;
import sqlapi.predicates.Predicate;

import java.util.List;

public interface JoinedTableReference extends TableReference {

    @NotNull TableReference getLeftTableReference();

    @NotNull TableReference getRightTableReference();

    @NotNull Predicate getPredicate();

    @Override default void setParameters(List<Object> parameters) {
        getLeftTableReference().setParameters(parameters);
        getRightTableReference().setParameters(parameters);
        getPredicate().setParameters(parameters);
    }
}
