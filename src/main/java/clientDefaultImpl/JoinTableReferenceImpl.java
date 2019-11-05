package clientDefaultImpl;

import api.JoinTableReference;
import api.TableReference;

public class JoinTableReferenceImpl implements JoinTableReference {

    private final Type type;
    private final TableReference left;
    private final TableReference right;
    private final SelectionPredicateImpl selectionPredicate;


    JoinTableReferenceImpl(Type type, TableReference left, TableReference right, SelectionPredicateImpl selectionPredicate) {
        this.type = type;
        this.left = left;
        this.right = right;
        this.selectionPredicate = selectionPredicate;
    }

    public Type getType() {
        return type;
    }


    @Override
    public TableReference getLeftTableReference() {
        return left;
    }

    @Override
    public TableReference getRightTableReference() {
        return right;
    }

    public final SelectionPredicateImpl getSelectionPredicate() {
        return selectionPredicate;
    }


}
