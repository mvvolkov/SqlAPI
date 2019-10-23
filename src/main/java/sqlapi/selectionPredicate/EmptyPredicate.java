package sqlapi.selectionPredicate;

/**
 * This class should be used in case when selection criteria is not specified.
 */
public class EmptyPredicate extends AbstractPredicate {

    public EmptyPredicate() {
        super(Type.EMPTY);
    }

    @Override
    public String toString() {
        return "";
    }
}
