package clientImpl.tableRef;

import api.tables.TableReference;
import api.predicates.Predicate;

public class TableRefFactory {

    private TableRefFactory() {
    }

    public static TableReference dbTable(String databaseName, String tableName) {
        return new DatabaseTableReferenceImpl(databaseName, tableName);
    }


    public static TableReference innerJoin(TableReference left, TableReference right,
                                           Predicate selectionPredicate) {
        return new JoinTableReferenceImpl(TableReference.TableRefType.INNER_JOIN, left, right,
                selectionPredicate);
    }


    public static TableReference leftOuterJoin(TableReference left, TableReference right,
                                               Predicate selectionPredicate) {
        return new JoinTableReferenceImpl(TableReference.TableRefType.LEFT_OUTER_JOIN, left,
                right, selectionPredicate);
    }


    public static TableReference rightOuterJoin(TableReference left, TableReference right,
                                                Predicate selectionPredicate) {
        return new JoinTableReferenceImpl(TableReference.TableRefType.RIGHT_OUTER_JOIN, left,
                right, selectionPredicate);
    }
}
