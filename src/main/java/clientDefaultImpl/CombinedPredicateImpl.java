package clientDefaultImpl;

import api.selectionPredicate.CombinedPredicate;
import api.selectionPredicate.Predicate;
import org.jetbrains.annotations.NotNull;

public final class CombinedPredicateImpl extends SelectionPredicateImpl implements CombinedPredicate {


    @NotNull
    protected final Predicate leftPredicate;

    @NotNull
    protected final Predicate rightPredicate;


    CombinedPredicateImpl(@NotNull Type type, @NotNull Predicate leftPredicate,
                          @NotNull Predicate rightPredicate) {
        super(type);
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
        return "(" + leftPredicate + ") " + this.getOperatorString() + " (" + rightPredicate + ")";
    }
}
