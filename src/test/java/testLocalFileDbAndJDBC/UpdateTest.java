package testLocalFileDbAndJDBC;

import clientImpl.columnExpr.ColumnExprFactory;
import clientImpl.assignment.AssignmentOperationFactory;
import clientImpl.predicates.PredicateFactory;
import clientImpl.queries.QueryFactory;
import org.junit.Test;
import sqlapi.exceptions.SqlException;
import sqlapi.queryResult.QueryResult;
import sqlapi.server.SqlServer;

import java.util.Arrays;
import java.util.Collections;

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
public class UpdateTest extends AbstractTestRunner {


    public UpdateTest(SqlServer sqlServer, String database) {
        super(sqlServer, database);
    }

    /**
     * UPDATE DB1.table1 SET column4='updtd';
     * SELECT * FROM DB1.table1;
     * <p>
     * column1, column2, column3, column4
     * 10, 30, test1, updtd
     * 11, 31, test2, updtd
     * 12, 32, test3, updtd
     * 13, 33, test2, updtd
     * 15, 34, test1, updtd
     * 16, null, test1, updtd
     */
    @Test
    public void testUpdateOneColumnNoPredicate() {
        System.out.println("testUpdateOneColumnNoPredicate:");
        try {

            sqlServer.executeQuery(QueryFactory.update(databaseName, "table1",
                    Collections.singletonList(AssignmentOperationFactory
                            .assign("column4",
                                    ColumnExprFactory.value("updtd")))));

            QueryResult queryResult = this.getTableData(databaseName, "table1");
            assertEquals(4, queryResult.getHeaders().size());
            assertEquals(6, queryResult.getRows().size());
            checkRowExists(queryResult, 10, 30, "test1", "updtd");
            checkRowExists(queryResult, 11, 31, "test2", "updtd");
            checkRowExists(queryResult, 12, 32, "test3", "updtd");
            checkRowExists(queryResult, 13, 33, "test2", "updtd");
            checkRowExists(queryResult, 15, 34, "test1", "updtd");
            checkRowExists(queryResult, 16, null, "test1", "updtd");

        } catch (SqlException se) {
            System.out.println(se.getMessage());
            fail();
        }
    }

    /**
     * UPDATE DB1.table1 SET column4='updtd', column2=37;
     * SELECT * FROM DB1.table1;
     * <p>
     * column1, column2, column3, column4
     * 10, 37, test1, updtd
     * 11, 37, test2, updtd
     * 12, 37, test3, updtd
     * 13, 37, test2, updtd
     * 15, 37, test1, updtd
     * 16, 37, test1, updtd
     */
    @Test
    public void testUpdateTwoColumnsNoPredicate() {
        System.out.println("testUpdateTwoColumnsNoPredicate:");
        try {

            sqlServer.executeQuery(QueryFactory.update(databaseName, "table1",
                    Arrays.asList(AssignmentOperationFactory
                                    .assign("column4", ColumnExprFactory.value("updtd")),
                            AssignmentOperationFactory
                                    .assign("column2",
                                            ColumnExprFactory.value(37)))));

            QueryResult queryResult = this.getTableData(databaseName, "table1");
            assertEquals(4, queryResult.getHeaders().size());
            assertEquals(6, queryResult.getRows().size());
            checkRowExists(queryResult, 10, 37, "test1", "updtd");
            checkRowExists(queryResult, 11, 37, "test2", "updtd");
            checkRowExists(queryResult, 12, 37, "test3", "updtd");
            checkRowExists(queryResult, 13, 37, "test2", "updtd");
            checkRowExists(queryResult, 15, 37, "test1", "updtd");
            checkRowExists(queryResult, 16, 37, "test1", "updtd");

        } catch (SqlException se) {
            System.out.println(se.getMessage());
            fail();
        }

    }

    /**
     * UPDATE DB1.table1 SET column4='updtd', column2=37 WHERE column4 IS NULL;
     * SELECT * FROM DB1.table1;
     * column1, column2, column3, column4
     * 10, 30, test1, t21
     * 11, 37, test2, updtd
     * 12, 32, test3, t43
     * 13, 33, test2, t653
     * 15, 37, test1, updtd
     * 16, 37, test1, updtd
     */
    @Test
    public void testUpdateTwoColumnsPredicateIsNull() {
        System.out.println("testUpdateTwoColumnsPredicateIsNull:");
        try {

            sqlServer.executeQuery(QueryFactory.update(databaseName, "table1",
                    Arrays.asList(AssignmentOperationFactory
                                    .assign("column4", ColumnExprFactory.value("updtd")),
                            AssignmentOperationFactory
                                    .assign("column2",
                                            ColumnExprFactory.value(37))),
                    PredicateFactory.isNull("column4")));

            QueryResult queryResult = this.getTableData(databaseName, "table1");
            assertEquals(4, queryResult.getHeaders().size());
            assertEquals(6, queryResult.getRows().size());
            checkRowExists(queryResult, 10, 30, "test1", "t21");
            checkRowExists(queryResult, 11, 37, "test2", "updtd");
            checkRowExists(queryResult, 12, 32, "test3", "t43");
            checkRowExists(queryResult, 13, 33, "test2", "t653");
            checkRowExists(queryResult, 15, 37, "test1", "updtd");
            checkRowExists(queryResult, 16, 37, "test1", "updtd");

        } catch (SqlException se) {
            System.out.println(se.getMessage());
            fail();
        }
    }

    /**
     * UPDATE DB1.table1 SET column1=((column1 + column2) + 11)
     * WHERE (column1 IS NOT NULL) AND (column2 IS NOT NULL);
     * SELECT * FROM DB1.table1;
     * <p>
     * column1, column2, column3, column4
     * 51, 30, test1, t21
     * 53, 31, test2, null
     * 55, 32, test3, t43
     * 57, 33, test2, t653
     * 60, 34, test1, null
     * 16, null, test1, null
     */
    @Test
    public void testUpdateColumnWithExpression() {
        System.out.println("testUpdateOneColumnNoPredicate:");
        try {

            sqlServer.executeQuery(QueryFactory.update(databaseName, "table1",
                    Collections.singletonList(AssignmentOperationFactory
                            .assign("column1", ColumnExprFactory.sum("column1", "column2")
                                    .add(ColumnExprFactory.value(11)))),
                    PredicateFactory.isNotNull("column2")
                            .and(PredicateFactory.isNotNull("column1")
                            )));

            QueryResult queryResult = this.getTableData(databaseName, "table1");
            assertEquals(4, queryResult.getHeaders().size());
            assertEquals(6, queryResult.getRows().size());
            checkRowExists(queryResult, 51, 30, "test1", "t21");
            checkRowExists(queryResult, 53, 31, "test2", null);
            checkRowExists(queryResult, 55, 32, "test3", "t43");
            checkRowExists(queryResult, 57, 33, "test2", "t653");
            checkRowExists(queryResult, 60, 34, "test1", null);
            checkRowExists(queryResult, 16, null, "test1", null);

        } catch (SqlException se) {
            System.out.println(se.getMessage());
            fail();
        }
    }
}
