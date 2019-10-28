package sqlapi.selectionPredicate;

/**
 * This class should be used in case when selection criteria is not specified.
 */
public class TruePredicate extends SelectionPredicate {

    public TruePredicate() {
        super(Type.TRUE);
    }

    @Override
    public String toString() {
        return "TRUE";
    }
}
