package sqlapi.selectionPredicate;

/**
 * This class should be used in case when selection criteria is not specified.
 */
public class EmptyPredicate extends SelectionPredicate {

    public EmptyPredicate() {
        super(Type.EMPTY);
    }

    @Override
    public String toString() {
        return "";
    }
}
