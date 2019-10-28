package sqlapi.selectionPredicate;

public class FalsePredicate extends SelectionPredicate {

    public FalsePredicate() {
        super(Type.FALSE);
    }

    @Override
    public String toString() {
        return "FALSE";
    }
}
