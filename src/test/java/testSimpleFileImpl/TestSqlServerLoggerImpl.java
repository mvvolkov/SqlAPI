package testSimpleFileImpl;

import api.SqlServer;
import api.exceptions.SqlException;
import clientImpl.AssignmentOperationImpl;
import clientImpl.columnExpr.ColumnExprFactory;
import clientImpl.metadata.ColumnMetadataFactory;
import clientImpl.predicates.PredicateFactory;
import clientImpl.queries.SqlQueryFactory;
import sqlFactory.SqlManagerFactory;

import java.util.Arrays;

public class TestSqlServerLoggerImpl {

    public static void main(String[] args) {

        SqlServer sqlServer = SqlManagerFactory.getPrintOutSqlManager();

        try {
            sqlServer.createDatabase("DB1");

            sqlServer.executeStatement(
                    SqlQueryFactory.createTable("DB1", "table1", Arrays.asList(
                            ColumnMetadataFactory.integerBuilder("column1").notNull()
                                    .primaryKey().build(),
                            ColumnMetadataFactory.integerBuilder("column2").build(),
                            ColumnMetadataFactory.varcharBuilder("column3", 20).notNull()
                                    .build())));

            sqlServer.executeStatement(
                    SqlQueryFactory.insert("DB1", "table2",
                            Arrays.asList(ColumnExprFactory.integer(10), ColumnExprFactory.integer(null), ColumnExprFactory.string("test"))));

            sqlServer.executeStatement(SqlQueryFactory.insert("DB1", "table1",
                    Arrays.asList("column1", "column2", "column3"),
                    Arrays.asList(ColumnExprFactory.integer(10), ColumnExprFactory.integer(null), ColumnExprFactory.string("test"))));

            sqlServer.executeStatement(SqlQueryFactory.delete("DB1", "table1"));

            sqlServer.executeStatement(SqlQueryFactory.delete("DB1", "table1",
                    PredicateFactory
                            .equals(ColumnExprFactory.columnRef("column1"),
                                    ColumnExprFactory.integer(3)).or(
                            PredicateFactory.greaterThan(
                                    ColumnExprFactory.columnRef("column2"),
                                    ColumnExprFactory.string("12"))).and(
                            PredicateFactory.equals(
                                    ColumnExprFactory.columnRef("column3"),
                                    ColumnExprFactory.integer(null)))));


            sqlServer.executeStatement(SqlQueryFactory.update("DB1", "table1",
                    Arrays.asList(new AssignmentOperationImpl("column1",
                                    ColumnExprFactory.integer(10)),
                            new AssignmentOperationImpl("column2",
                                    ColumnExprFactory.string("test3"))),
                    PredicateFactory
                            .lessThan(ColumnExprFactory.columnRef("column3"),
                                    ColumnExprFactory.string("abs"))));


            sqlServer.executeStatement(SqlQueryFactory.update("DB1", "table1",
                    Arrays.asList(new AssignmentOperationImpl("column1",
                                    ColumnExprFactory.add(ColumnExprFactory.columnRef("column1"), ColumnExprFactory.integer(1))
                            ),
                            new AssignmentOperationImpl("column2",
                                    ColumnExprFactory.string("test3"))),
                    PredicateFactory.in(ColumnExprFactory.columnRef("column3"),
                            Arrays.asList(ColumnExprFactory.string("12"), ColumnExprFactory.string("13"),
                                    ColumnExprFactory.string("14")))
            ));


        } catch (SqlException e) {
            System.out.println(e.getMessage());
        }


    }
}
