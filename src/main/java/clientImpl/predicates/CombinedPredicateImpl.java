package clientImpl.predicates;

import sqlapi.predicates.combined.CombinedPredicate;
import sqlapi.predicates.Predicate;
import org.jetbrains.annotations.NotNull;

abstract class CombinedPredicateImpl extends PredicateImpl
        implements CombinedPredicate {


    @NotNull
    private final Predicate leftPredicate;

    @NotNull
    private final Predicate rightPredicate;


    CombinedPredicateImpl(@NotNull Predicate leftPredicate,
                          @NotNull Predicate rightPredicate) {
        this.leftPredicate = leftPredicate;
        this.rightPredicate = rightPredicate;
    }

    @NotNull
    @Override
    public Predicate getLeftPredicate() {
        return leftPredicate;
    }

    @NotNull
    @Override
    public Predicate getRightPredicate() {
        return rightPredicate;
    }


    @Override
    public String toString() {
        return "(" + leftPredicate + ") " + this.getOperatorString() + " (" +
                rightPredicate + ")";
    }

    abstract protected String getOperatorString();
}
