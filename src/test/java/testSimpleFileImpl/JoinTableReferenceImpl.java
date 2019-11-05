package testSimpleFileImpl;

import sqlapi.JoinTableReference;
import sqlapi.TableReference;
import sqlapi.selectionPredicate.SelectionPredicate;

public class JoinTableReferenceImpl implements JoinTableReference {

    private final Type type;
    private final TableReference left;
    private final TableReference right;
    private final SelectionPredicate selectionPredicate;


    JoinTableReferenceImpl(Type type, TableReference left, TableReference right, SelectionPredicate selectionPredicate) {
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

    public final SelectionPredicate getSelectionPredicate() {
        return selectionPredicate;
    }


}
