package clientImpl.tableRef;

import clientImpl.predicates.PredicateFactory;
import sqlapi.queries.SelectExpression;
import sqlapi.tables.TableReference;
import sqlapi.predicates.Predicate;

public class TableRefFactory {

    private TableRefFactory() {
    }


    public static TableReference dbTable(String databaseName, String tableName) {
        return new DatabaseTableReferenceImpl(databaseName, tableName);
    }


    public static TableReference innerJoin(TableReference left, TableReference right,
                                           Predicate selectionPredicate) {
        return new JoinedTableReferenceImpl(TableReference.TableRefType.INNER_JOIN, left,
                right,
                selectionPredicate);
    }

    public static TableReference innerJoin(TableReference left, String dbName, String tableName,
                                           Predicate selectionPredicate) {
        return innerJoin(left, dbTable(dbName, tableName), selectionPredicate);
    }

    public static TableReference innerJoin(String dbName, String tableName, TableReference right,
                                           Predicate selectionPredicate) {
        return innerJoin(dbTable(dbName, tableName), right, selectionPredicate);
    }

    public static TableReference innerJoin(String dbName1, String tableName1, String dbName2, String tableName2,
                                           Predicate selectionPredicate) {
        return innerJoin(dbTable(dbName1, tableName1), dbTable(dbName2, tableName2), selectionPredicate);
    }


    public static TableReference innerJoin(TableReference left, TableReference right) {
        return innerJoin(left, right, PredicateFactory.empty());
    }

    public static TableReference innerJoin(TableReference left, String dbName, String tableName) {
        return innerJoin(left, dbTable(dbName, tableName));
    }

    public static TableReference innerJoin(String dbName, String tableName, TableReference right) {
        return innerJoin(dbTable(dbName, tableName), right);
    }

    public static TableReference innerJoin(String dbName1, String tableName1, String dbName2, String tableName2) {
        return innerJoin(dbTable(dbName1, tableName1), dbTable(dbName2, tableName2));
    }


    public static TableReference leftOuterJoin(TableReference left, TableReference right,
                                               Predicate selectionPredicate) {
        return new JoinedTableReferenceImpl(TableReference.TableRefType.LEFT_OUTER_JOIN,
                left,
                right, selectionPredicate);
    }

    public static TableReference leftOuterJoin(TableReference left, String dbName, String tableName,
                                               Predicate selectionPredicate) {
        return leftOuterJoin(left, dbTable(dbName, tableName), selectionPredicate);
    }

    public static TableReference leftOuterJoin(String dbName, String tableName, TableReference right,
                                               Predicate selectionPredicate) {
        return leftOuterJoin(dbTable(dbName, tableName), right, selectionPredicate);
    }

    public static TableReference leftOuterJoin(String dbName1, String tableName1, String dbName2, String tableName2,
                                               Predicate selectionPredicate) {
        return leftOuterJoin(dbTable(dbName1, tableName1), dbTable(dbName2, tableName2), selectionPredicate);
    }

    public static TableReference leftOuterJoin(TableReference left, TableReference right) {
        return leftOuterJoin(left, right, PredicateFactory.empty());
    }

    public static TableReference leftOuterJoin(TableReference left, String dbName, String tableName) {
        return leftOuterJoin(left, dbTable(dbName, tableName));
    }

    public static TableReference leftOuterJoin(String dbName, String tableName, TableReference right) {
        return leftOuterJoin(dbTable(dbName, tableName), right);
    }

    public static TableReference leftOuterJoin(String dbName1, String tableName1, String dbName2, String tableName2) {
        return leftOuterJoin(dbTable(dbName1, tableName1), dbTable(dbName2, tableName2));
    }


    public static TableReference rightOuterJoin(TableReference left, TableReference right,
                                                Predicate selectionPredicate) {
        return new JoinedTableReferenceImpl(TableReference.TableRefType.RIGHT_OUTER_JOIN,
                left,
                right, selectionPredicate);
    }

    public static TableReference rightOuterJoin(TableReference left, String dbName, String tableName,
                                               Predicate selectionPredicate) {
        return rightOuterJoin(left, dbTable(dbName, tableName), selectionPredicate);
    }

    public static TableReference rightOuterJoin(String dbName, String tableName, TableReference right,
                                               Predicate selectionPredicate) {
        return rightOuterJoin(dbTable(dbName, tableName), right, selectionPredicate);
    }

    public static TableReference rightOuterJoin(String dbName1, String tableName1, String dbName2, String tableName2,
                                               Predicate selectionPredicate) {
        return rightOuterJoin(dbTable(dbName1, tableName1), dbTable(dbName2, tableName2), selectionPredicate);
    }

    public static TableReference rightOuterJoin(TableReference left, TableReference right) {
        return rightOuterJoin(left, right, PredicateFactory.empty());
    }

    public static TableReference rightOuterJoin(TableReference left, String dbName, String tableName) {
        return rightOuterJoin(left, dbTable(dbName, tableName));
    }

    public static TableReference rightOuterJoin(String dbName, String tableName, TableReference right) {
        return rightOuterJoin(dbTable(dbName, tableName), right);
    }

    public static TableReference rightOuterJoin(String dbName1, String tableName1, String dbName2, String tableName2) {
        return rightOuterJoin(dbTable(dbName1, tableName1), dbTable(dbName2, tableName2));
    }

    public static TableReference tableFromSelect(SelectExpression selectExpression,
                                                 String alias) {
        return new TableFromSelectReferenceImpl(selectExpression, alias);
    }

    public static TableReference tableFromSelect(SelectExpression selectExpression) {
        return new TableFromSelectReferenceImpl(selectExpression, "");
    }
}
