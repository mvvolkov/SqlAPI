package clientImpl.predicates;

final class EmptyPredicateImpl extends PredicateImpl {

    EmptyPredicateImpl() {
        super(Type.EMPTY);
    }

    @Override public String toString() {
        return "";
    }
}
