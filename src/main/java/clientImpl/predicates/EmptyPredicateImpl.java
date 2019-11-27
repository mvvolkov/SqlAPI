package clientImpl.predicates;

import sqlapi.predicates.EmptyPredicate;

final class EmptyPredicateImpl extends PredicateImpl implements EmptyPredicate {

    EmptyPredicateImpl() {
    }

    @Override
    public String toString() {
        return "";
    }
}
