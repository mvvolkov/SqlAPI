package sqlapi.exceptions;

import org.jetbrains.annotations.NotNull;
import sqlapi.predicates.Predicate;

public class UnsupportedPredicateTypeException extends SqlException {

    @NotNull
    private final Predicate predicate;


    public UnsupportedPredicateTypeException(@NotNull Predicate predicate) {
        this.predicate = predicate;
    }

    @NotNull
    public Predicate getPredicate() {
        return predicate;
    }

    @Override
    public String getMessage() {
        return "Unsupported predicate type: " + predicate;
    }
}
