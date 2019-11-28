package sqlapi.exceptions;

import sqlapi.predicates.Predicate;

public class UnsupportedPredicateTypeException extends SqlException {

    private final Predicate predicate;


    public UnsupportedPredicateTypeException(Predicate predicate) {
        this.predicate = predicate;
    }

    public Predicate getPredicate() {
        return predicate;
    }

    @Override
    public String getMessage() {
        return "Unsupported predicate type: " + predicate;
    }
}
