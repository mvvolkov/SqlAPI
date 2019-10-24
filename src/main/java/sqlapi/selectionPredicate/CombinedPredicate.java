package sqlapi.selectionPredicate;

import org.jetbrains.annotations.NotNull;

public final class CombinedPredicate extends SelectionPredicate {


    @NotNull
    protected final SelectionPredicate leftPredicate;

    @NotNull
    protected final SelectionPredicate rightPredicate;


    CombinedPredicate(@NotNull Type type, @NotNull SelectionPredicate leftPredicate,
                      @NotNull SelectionPredicate rightPredicate) {
        super(type);
        this.leftPredicate = leftPredicate;
        this.rightPredicate = rightPredicate;
    }

    @NotNull
    public SelectionPredicate getLeftPredicate() {
        return leftPredicate;
    }

    @NotNull
    public SelectionPredicate getRightPredicate() {
        return rightPredicate;
    }

    @Override
    public String toString() {
        return "(" + leftPredicate + ") " + this.getOperatorString() + " (" + rightPredicate + ")";
    }
}
