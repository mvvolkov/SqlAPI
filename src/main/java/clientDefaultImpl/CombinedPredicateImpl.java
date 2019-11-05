package clientDefaultImpl;

import api.selectionPredicate.Predicate;
import org.jetbrains.annotations.NotNull;

public final class CombinedPredicateImpl extends SelectionPredicateImpl {


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
    public Predicate getLeftPredicate() {
        return leftPredicate;
    }

    @NotNull
    public Predicate getRightPredicate() {
        return rightPredicate;
    }

    @Override
    public String toString() {
        return "(" + leftPredicate + ") " + this.getOperatorString() + " (" + rightPredicate + ")";
    }
}
