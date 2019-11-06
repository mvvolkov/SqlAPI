package api.selectionPredicate;

public interface CombinedPredicate extends Predicate{
    Predicate getLeftPredicate();
    Predicate getRightPredicate();
}
