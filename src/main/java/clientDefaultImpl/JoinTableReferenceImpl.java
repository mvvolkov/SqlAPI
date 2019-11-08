package clientDefaultImpl;

import api.JoinTableReference;
import api.TableReference;
import api.selectionPredicate.Predicate;

public class JoinTableReferenceImpl implements JoinTableReference {

    private final Type joinType;
    private final TableReference left;
    private final TableReference right;
    private final Predicate selectionPredicate;


    JoinTableReferenceImpl(Type joinType, TableReference left, TableReference right, Predicate selectionPredicate) {
        this.joinType = joinType;
        this.left = left;
        this.right = right;
        this.selectionPredicate = selectionPredicate;
    }

    @Override
    public TableReference getLeftTableReference() {
        return left;
    }

    @Override
    public TableReference getRightTableReference() {
        return right;
    }

    @Override
    public Predicate getSelectionPredicate() {
        return selectionPredicate;
    }


    @Override
    public Type getType() {
        return joinType;
    }
}