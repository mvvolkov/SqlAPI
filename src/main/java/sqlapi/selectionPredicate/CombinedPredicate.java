package sqlapi.selectionPredicate;

public final class CombinedPredicate extends AbstractPredicate {


    /**
     * Left predicate.
     */
    protected final AbstractPredicate left;

    /**
     * Right predicate.
     */
    protected final AbstractPredicate right;

    /**
     * Instance of this type can be obtained with static method only.
     *
     * @param type  - binary operation type.
     * @param left  - left predicate.
     * @param right - right predicate.
     */
    CombinedPredicate(Type type, AbstractPredicate left, AbstractPredicate right) {
        super(type);
        this.left = left;
        this.right = right;
    }

    public AbstractPredicate getLeft() {
        return left;
    }

    public AbstractPredicate getRight() {
        return right;
    }

    @Override
    public String toString() {
        return "(" + left + ") " + this.getOperatorString() + " (" + right + ")";
    }
}
