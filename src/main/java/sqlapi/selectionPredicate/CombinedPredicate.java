package sqlapi.selectionPredicate;

import org.jetbrains.annotations.NotNull;

public final class CombinedPredicate extends AbstractPredicate {


    @NotNull
    protected final AbstractPredicate leftPredicate;

    @NotNull
    protected final AbstractPredicate rightPredicate;


    CombinedPredicate(@NotNull Type type, @NotNull AbstractPredicate leftPredicate,
                      @NotNull AbstractPredicate rightPredicate) {
        super(type);
        this.leftPredicate = leftPredicate;
        this.rightPredicate = rightPredicate;
    }

    @NotNull
    public AbstractPredicate getLeftPredicate() {
        return leftPredicate;
    }

    @NotNull
    public AbstractPredicate getRightPredicate() {
        return rightPredicate;
    }

    @Override
    public String toString() {
        return "(" + leftPredicate + ") " + this.getOperatorString() + " (" + rightPredicate + ")";
    }
}
