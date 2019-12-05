package clientImpl.predicates;

import sqlapi.predicates.EmptyPredicate;

import java.util.ArrayDeque;

final class EmptyPredicateImpl extends PredicateImpl implements EmptyPredicate {

    EmptyPredicateImpl() {
    }

    @Override
    public String toString() {
        return "";
    }

}
