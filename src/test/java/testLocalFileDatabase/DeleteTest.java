package testLocalFileDatabase;

import clientImpl.predicates.PredicateFactory;
import clientImpl.queries.QueryFactory;
import org.junit.Test;
import sqlapi.exceptions.SqlException;
import sqlapi.queryResult.QueryResult;

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
public class DeleteTest extends AbstractLocalFileDatabaseTest {

    /**
     * DELETE FROM DB1.table1;
     * SELECT * FROM DB1.table1;
     * <p>
     * column1, column2, column3, column4
     */
    @Test
    public void testDeleteAll() {

        System.out.println("testDeleteAll:");
        try {

            sqlServer.executeQuery(QueryFactory.delete(databaseName, "table1"));

            QueryResult queryResult = this.getTableData(databaseName, "table1");
            checkHeaders(queryResult.getHeaders(), "column1", "column2", "column3",
                    "column4");
            assertEquals(0, queryResult.getRows().size());

        } catch (SqlException se) {
            System.out.println(se.getMessage());
            fail();
        }
    }

    /**
     * DELETE FROM DB1.table1 WHERE column4 IS NULL;
     * SELECT * FROM DB1.table1;
     * <p>
     * column1, column2, column3, column4
     * 10, 30, test1, t21
     * 12, 32, test3, t43
     * 13, 33, test2, t653
     */
    @Test
    public void testDeletePredicateIsNull() {

        System.out.println("testDeletePredicateIsNull:");
        try {
            QueryResult queryResult = this.getTableData(databaseName, "table1");
            assertEquals(4, queryResult.getHeaders().size());
            assertEquals(6, queryResult.getRows().size());

            sqlServer.executeQuery(QueryFactory.delete(databaseName, "table1",
                    PredicateFactory.isNull("column4")));

            queryResult = this.getTableData(databaseName, "table1");
            assertEquals(4, queryResult.getHeaders().size());
            assertEquals(3, queryResult.getRows().size());
            checkRowExists(queryResult, 10, 30, "test1", "t21");
            checkRowExists(queryResult, 12, 32, "test3", "t43");
            checkRowExists(queryResult, 13, 33, "test2", "t653");

        } catch (SqlException se) {
            System.out.println(se.getMessage());
            fail();
        }
    }

    /**
     * DELETE FROM DB1.table1 WHERE column4 IS NOT NULL;
     * SELECT * FROM DB1.table1;
     * <p>
     * column1, column2, column3, column4
     * 11, 31, test2, null
     * 15, 34, test1, null
     * 16, null, test1, null
     */
    @Test
    public void testDeletePredicateIsNotNull() {

        System.out.println("testDeletePredicateIsNotNull:");
        try {
            QueryResult queryResult = this.getTableData(databaseName, "table1");
            assertEquals(4, queryResult.getHeaders().size());
            assertEquals(6, queryResult.getRows().size());

            sqlServer.executeQuery(QueryFactory.delete(databaseName, "table1",
                    PredicateFactory.isNotNull("column4")));

            queryResult = this.getTableData(databaseName, "table1");
            assertEquals(4, queryResult.getHeaders().size());
            assertEquals(3, queryResult.getRows().size());
            checkRowExists(queryResult, 11, 31, "test2", null);
            checkRowExists(queryResult, 15, 34, "test1", null);
            checkRowExists(queryResult, 16, null, "test1", null);

        } catch (SqlException se) {
            System.out.println(se.getMessage());
            fail();
        }
    }
}
