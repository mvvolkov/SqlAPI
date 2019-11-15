package clientImpl.tableRef;

import api.tables.JoinedTableReference;
import api.tables.TableReference;
import api.predicates.Predicate;

public class JoinedTableReferenceImpl implements JoinedTableReference {

    private final TableRefType joinType;
    private final TableReference left;
    private final TableReference right;
    private final Predicate selectionPredicate;


    JoinedTableReferenceImpl(TableRefType joinType, TableReference left, TableReference right,
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
