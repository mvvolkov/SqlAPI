package sqlapi.predicates;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface CombinedPredicate extends Predicate {

    @NotNull Predicate getLeftPredicate();

    @NotNull Predicate getRightPredicate();

    @Override default void setParameters(List<Object> parameters) {
        getLeftPredicate().setParameters(parameters);
        getRightPredicate().setParameters(parameters);
    }
}
