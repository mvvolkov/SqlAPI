package sqlapi.predicates;

import org.jetbrains.annotations.NotNull;

public interface Predicate {

    @NotNull Predicate and(@NotNull Predicate predicate);

    @NotNull Predicate or(@NotNull Predicate predicate);

    default boolean isEmpty() {
        return false;
    }
}
