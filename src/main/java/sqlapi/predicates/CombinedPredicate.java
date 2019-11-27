package sqlapi.predicates;

import org.jetbrains.annotations.NotNull;

public interface CombinedPredicate extends Predicate {

    @NotNull Predicate getLeftPredicate();

    @NotNull Predicate getRightPredicate();
}
