package sqlapi.predicates;

public interface EmptyPredicate extends Predicate {

    @Override
    default boolean isEmpty() {
        return true;
    }
}
