package sqlapi.predicates.combined;

import org.jetbrains.annotations.NotNull;
import sqlapi.predicates.Predicate;

import java.util.List;

public interface CombinedPredicate extends Predicate {

    @NotNull Predicate getLeftPredicate();

    @NotNull Predicate getRightPredicate();

    @Override default void setParameters(List<Object> parameters) {
        getLeftPredicate().setParameters(parameters);
        getRightPredicate().setParameters(parameters);
    }
}
