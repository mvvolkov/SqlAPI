package testLocalFileDbAndJDBC;

import clientImpl.assignment.AssignmentOperationFactory;
import clientImpl.columnExpr.ColumnExprFactory;
import clientImpl.predicates.PredicateFactory;
import clientImpl.queries.QueryFactory;
import clientImpl.tables.TableRefFactory;
import org.junit.Test;
import sqlapi.exceptions.SqlException;
import sqlapi.queries.SelectQuery;
import sqlapi.queries.UpdateQuery;
import sqlapi.queryResult.QueryResult;
import sqlapi.server.SqlServer;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


/**
 * Before each test we have two tables:
 * <p>
 * SELECT * FROM DB1.table1;
 * <p>
 * column1, column2, column3, column4
 * 10, 30, test1, t21
 * 11, 31, test2, null
 * 12, 32, test3, t43
 * 13, 33, test2, t653
 * 15, 34, test1, null
 * 16, null, test1, null
 * <p>
 * <p>
 * SELECT * FROM DB1.table2;
 * <p>
 * column5, column3
 * 22, test3
 * 23, test2
 * 25, test4
 */
public class ParametrizedQueriesTest extends AbstractTestRunner {

    public ParametrizedQueriesTest(SqlServer sqlServer,
                                   String databaseName) {
        super(sqlServer, databaseName);
    }

    @Test
    public void testParametrizedUpdateQuery() {

        try {
            UpdateQuery query = QueryFactory.update(databaseName, "table1",
                    Arrays.asList(
                            AssignmentOperationFactory.assign("column2",
                                    ColumnExprFactory.columnRef("column1")
                                            .add(ColumnExprFactory.parameter())),
                            AssignmentOperationFactory.assign("column4",
                                    ColumnExprFactory.parameter())
                    ),
                    PredicateFactory
                            .equals("column3", ColumnExprFactory.parameter()));


            sqlServer.executeQuery(query, 5, "t50", "test1");

            QueryResult queryResult = this.getTableData(databaseName, "table1");
            printResultSet(queryResult);

            checkHeaders(queryResult.getHeaders(), "column1", "column2", "column3",
                    "column4");
            assertEquals(6, queryResult.getRows().size());
            checkRowExists(queryResult, 10, 15, "test1", "t50");
            checkRowExists(queryResult, 11, 31, "test2", null);
            checkRowExists(queryResult, 12, 32, "test3", "t43");
            checkRowExists(queryResult, 13, 33, "test2", "t653");
            checkRowExists(queryResult, 15, 20, "test1", "t50");
            checkRowExists(queryResult, 16, 21, "test1", "t50");

            sqlServer.executeQuery(query, 7, "t30", "test2");

            queryResult = this.getTableData(databaseName, "table1");
            printResultSet(queryResult);

            checkHeaders(queryResult.getHeaders(), "column1", "column2", "column3",
                    "column4");
            assertEquals(6, queryResult.getRows().size());
            checkRowExists(queryResult, 10, 15, "test1", "t50");
            checkRowExists(queryResult, 11, 18, "test2", "t30");
            checkRowExists(queryResult, 12, 32, "test3", "t43");
            checkRowExists(queryResult, 13, 20, "test2", "t30");
            checkRowExists(queryResult, 15, 20, "test1", "t50");
            checkRowExists(queryResult, 16, 21, "test1", "t50");

            sqlServer.executeQuery(query, 14, "d45", "test3");

            queryResult = this.getTableData(databaseName, "table1");
            printResultSet(queryResult);

            checkHeaders(queryResult.getHeaders(), "column1", "column2", "column3",
                    "column4");
            assertEquals(6, queryResult.getRows().size());
            checkRowExists(queryResult, 10, 15, "test1", "t50");
            checkRowExists(queryResult, 11, 18, "test2", "t30");
            checkRowExists(queryResult, 12, 26, "test3", "d45");
            checkRowExists(queryResult, 13, 20, "test2", "t30");
            checkRowExists(queryResult, 15, 20, "test1", "t50");
            checkRowExists(queryResult, 16, 21, "test1", "t50");


        } catch (SqlException se) {
            System.out.println(se.getMessage());
            fail();
        }
    }

    @Test
    public void testParametrizedSelectQuery() {

        try {
            SelectQuery query =
                    QueryFactory.select(TableRefFactory.dbTable(databaseName, "table1"),
                            Arrays.asList(ColumnExprFactory.sumWithAlias("column1",
                                    ColumnExprFactory.parameter(), "C1"),
                                    ColumnExprFactory.parameterWithAlias("C2")),
                            PredicateFactory.greaterThan("column1",
                                    ColumnExprFactory.parameter()));

            QueryResult queryResult = sqlServer.getQueryResult(query, 3, 30, 10);

            printResultSet(queryResult);

            checkHeaders(queryResult.getHeaders(), "C1", "C2");
            assertEquals(5, queryResult.getRows().size());
            checkRowExists(queryResult, 14, 30);
            checkRowExists(queryResult, 15, 30);
            checkRowExists(queryResult, 16, 30);
            checkRowExists(queryResult, 18, 30);
            checkRowExists(queryResult, 19, 30);

            queryResult = sqlServer.getQueryResult(query, 8, "25", 12);

            printResultSet(queryResult);

            checkHeaders(queryResult.getHeaders(), "C1", "C2");
            assertEquals(3, queryResult.getRows().size());
            checkRowExists(queryResult, 21, "25");
            checkRowExists(queryResult, 23, "25");
            checkRowExists(queryResult, 24, "25");

        } catch (SqlException se) {
            System.out.println(se.getMessage());
            fail();
        }
    }
}
