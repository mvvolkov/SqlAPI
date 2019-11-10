package clientImpl.tableRef;

import api.TableReference;
import api.predicates.Predicate;

public class TableRefFactory {

    private TableRefFactory() {
    }

    public static TableReference dbTable(String databaseName, String tableName) {
        return new DatabaseTableReferenceImpl(databaseName, tableName);
    }


    public static TableReference innerJoin(TableReference left, TableReference right,
                                           Predicate selectionPredicate) {
        return new JoinTableReferenceImpl(TableReference.Type.INNER_JOIN, left, right,
                selectionPredicate);
    }


    public static TableReference leftOuterJoin(TableReference left, TableReference right,
                                               Predicate selectionPredicate) {
        return new JoinTableReferenceImpl(TableReference.Type.LEFT_OUTER_JOIN, left,
                right, selectionPredicate);
    }


    public static TableReference rightOuterJoin(TableReference left, TableReference right,
                                                Predicate selectionPredicate) {
        return new JoinTableReferenceImpl(TableReference.Type.RIGHT_OUTER_JOIN, left,
                right, selectionPredicate);
    }
}
