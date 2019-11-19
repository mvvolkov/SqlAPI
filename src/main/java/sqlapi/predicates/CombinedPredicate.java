package sqlapi.predicates;

public interface CombinedPredicate extends Predicate{
    Predicate getLeftPredicate();
    Predicate getRightPredicate();
}
