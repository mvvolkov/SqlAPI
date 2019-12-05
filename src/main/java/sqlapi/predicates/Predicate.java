package sqlapi.predicates;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;

public interface Predicate {

    @NotNull Predicate and(@NotNull Predicate predicate);

    @NotNull Predicate or(@NotNull Predicate predicate);

    default boolean isEmpty() {
        return false;
    }

    void setParameters(ArrayDeque<Object> parameters);
}
