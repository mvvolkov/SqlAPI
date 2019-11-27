package clientImpl.tables;

import clientImpl.predicates.PredicateFactory;
import org.jetbrains.annotations.NotNull;
import sqlapi.predicates.Predicate;
import sqlapi.queries.SelectQuery;
import sqlapi.tables.*;

public class TableRefFactory {

    private TableRefFactory() {
    }


    public static @NotNull DatabaseTableReference dbTable(@NotNull String databaseName, @NotNull String tableName) {
        return new DatabaseTableReferenceImpl(databaseName, tableName);
    }

    // ********************* INNER JOIN *****************************

    public static @NotNull InnerJoinTableReference innerJoin(@NotNull TableReference left, @NotNull TableReference right,
                                                             Predicate selectionPredicate) {
        return new InnerJoinTableReferenceImpl(left, right, selectionPredicate);
    }

    public static @NotNull InnerJoinTableReference innerJoin(@NotNull TableReference left, @NotNull String dbName, @NotNull String tableName,
                                                             Predicate selectionPredicate) {
        return innerJoin(left, dbTable(dbName, tableName), selectionPredicate);
    }

    public static @NotNull InnerJoinTableReference innerJoin(@NotNull String dbName, @NotNull String tableName, @NotNull TableReference right,
                                                             Predicate selectionPredicate) {
        return innerJoin(dbTable(dbName, tableName), right, selectionPredicate);
    }

    public static @NotNull InnerJoinTableReference innerJoin(@NotNull String dbName1, @NotNull String tableName1, @NotNull String dbName2, @NotNull String tableName2,
                                                             Predicate selectionPredicate) {
        return innerJoin(dbTable(dbName1, tableName1), dbTable(dbName2, tableName2), selectionPredicate);
    }


    public static @NotNull InnerJoinTableReference innerJoin(@NotNull TableReference left, @NotNull TableReference right) {
        return innerJoin(left, right, PredicateFactory.empty());
    }

    public static @NotNull InnerJoinTableReference innerJoin(@NotNull TableReference left, @NotNull String dbName, @NotNull String tableName) {
        return innerJoin(left, dbTable(dbName, tableName));
    }

    public static @NotNull InnerJoinTableReference innerJoin(@NotNull String dbName, @NotNull String tableName, @NotNull TableReference right) {
        return innerJoin(dbTable(dbName, tableName), right);
    }

    public static @NotNull TableReference innerJoin(@NotNull String dbName1, @NotNull String tableName1, @NotNull String dbName2, @NotNull String tableName2) {
        return innerJoin(dbTable(dbName1, tableName1), dbTable(dbName2, tableName2));
    }


    // ********************* LEFT OUTER JOIN *****************************


    public static @NotNull LeftOuterJoinTableReference leftOuterJoin(@NotNull TableReference left, @NotNull TableReference right,
                                                                     Predicate selectionPredicate) {
        return new LeftOuterJoinTableReferenceImpl(left, right, selectionPredicate);
    }

    public static @NotNull LeftOuterJoinTableReference leftOuterJoin(@NotNull TableReference left, @NotNull String dbName, @NotNull String tableName,
                                                                     Predicate selectionPredicate) {
        return leftOuterJoin(left, dbTable(dbName, tableName), selectionPredicate);
    }

    public static @NotNull LeftOuterJoinTableReference leftOuterJoin(@NotNull String dbName, @NotNull String tableName, @NotNull TableReference right,
                                                                     Predicate selectionPredicate) {
        return leftOuterJoin(dbTable(dbName, tableName), right, selectionPredicate);
    }

    public static @NotNull LeftOuterJoinTableReference leftOuterJoin(@NotNull String dbName1, @NotNull String tableName1, @NotNull String dbName2, @NotNull String tableName2,
                                                                     Predicate selectionPredicate) {
        return leftOuterJoin(dbTable(dbName1, tableName1), dbTable(dbName2, tableName2), selectionPredicate);
    }

    public static @NotNull LeftOuterJoinTableReference leftOuterJoin(@NotNull TableReference left, @NotNull TableReference right) {
        return leftOuterJoin(left, right, PredicateFactory.empty());
    }

    public static @NotNull LeftOuterJoinTableReference leftOuterJoin(@NotNull TableReference left, @NotNull String dbName, @NotNull String tableName) {
        return leftOuterJoin(left, dbTable(dbName, tableName));
    }

    public static @NotNull LeftOuterJoinTableReference leftOuterJoin(@NotNull String dbName, @NotNull String tableName, @NotNull TableReference right) {
        return leftOuterJoin(dbTable(dbName, tableName), right);
    }

    public static @NotNull LeftOuterJoinTableReference leftOuterJoin(@NotNull String dbName1, @NotNull String tableName1, @NotNull String dbName2, @NotNull String tableName2) {
        return leftOuterJoin(dbTable(dbName1, tableName1), dbTable(dbName2, tableName2));
    }

    // ********************* RIGHT OUTER JOIN *****************************

    public static @NotNull RightOuterJoinTableReference rightOuterJoin(@NotNull TableReference left, @NotNull TableReference right,
                                                                       Predicate selectionPredicate) {
        return new RightOuterJoinTableReferenceImpl(left, right, selectionPredicate);
    }

    public static @NotNull RightOuterJoinTableReference rightOuterJoin(@NotNull TableReference left, @NotNull String dbName, @NotNull String tableName,
                                                                       Predicate selectionPredicate) {
        return rightOuterJoin(left, dbTable(dbName, tableName), selectionPredicate);
    }

    public static @NotNull RightOuterJoinTableReference rightOuterJoin(@NotNull String dbName, @NotNull String tableName, @NotNull TableReference right,
                                                                       Predicate selectionPredicate) {
        return rightOuterJoin(dbTable(dbName, tableName), right, selectionPredicate);
    }

    public static @NotNull RightOuterJoinTableReference rightOuterJoin(@NotNull String dbName1, @NotNull String tableName1, @NotNull String dbName2, @NotNull String tableName2,
                                                                       Predicate selectionPredicate) {
        return rightOuterJoin(dbTable(dbName1, tableName1), dbTable(dbName2, tableName2), selectionPredicate);
    }

    public static @NotNull RightOuterJoinTableReference rightOuterJoin(@NotNull TableReference left, @NotNull TableReference right) {
        return rightOuterJoin(left, right, PredicateFactory.empty());
    }

    public static @NotNull RightOuterJoinTableReference rightOuterJoin(@NotNull TableReference left, @NotNull String dbName, @NotNull String tableName) {
        return rightOuterJoin(left, dbTable(dbName, tableName));
    }

    public static @NotNull RightOuterJoinTableReference rightOuterJoin(@NotNull String dbName, @NotNull String tableName, @NotNull TableReference right) {
        return rightOuterJoin(dbTable(dbName, tableName), right);
    }

    public static @NotNull RightOuterJoinTableReference rightOuterJoin(@NotNull String dbName1, @NotNull String tableName1, @NotNull String dbName2, @NotNull String tableName2) {
        return rightOuterJoin(dbTable(dbName1, tableName1), dbTable(dbName2, tableName2));
    }

    // ********************* TABLE FROM SELECT *****************************

    public static @NotNull TableFromSelectReference tableFromSelect(@NotNull SelectQuery selectQuery,
                                                                    @NotNull String alias) {
        return new TableFromSelectReferenceImpl(selectQuery, alias);
    }

    public static @NotNull TableFromSelectReference tableFromSelect(@NotNull SelectQuery selectQuery) {
        return new TableFromSelectReferenceImpl(selectQuery, "");
    }
}
