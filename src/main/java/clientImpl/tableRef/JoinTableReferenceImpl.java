package clientImpl.tableRef;

import api.tables.JoinTableReference;
import api.tables.TableReference;
import api.predicates.Predicate;

public class JoinTableReferenceImpl implements JoinTableReference {

    private final TableRefType joinType;
    private final TableReference left;
    private final TableReference right;
    private final Predicate selectionPredicate;


    JoinTableReferenceImpl(TableRefType joinType, TableReference left, TableReference right,
                           Predicate selectionPredicate) {
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
    public Predicate getPredicate() {
        return selectionPredicate;
    }


    @Override
    public TableRefType getTableRefType() {
        return joinType;
    }
}
