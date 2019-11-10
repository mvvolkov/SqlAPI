package api.predicates;

public interface CombinedPredicate extends Predicate{
    Predicate getLeftPredicate();
    Predicate getRightPredicate();
}
