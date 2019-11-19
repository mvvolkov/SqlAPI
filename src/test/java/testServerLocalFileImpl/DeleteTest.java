package testServerLocalFileImpl;

import sqlapi.exceptions.SqlException;
import sqlapi.selectResult.ResultSet;
import clientImpl.columnExpr.ColumnExprFactory;
import clientImpl.predicates.PredicateFactory;
import clientImpl.queries.SqlQueryFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class DeleteTest extends AbstractServerLocalFileTest {

    @Test
    public void testDeleteAll() {

        System.out.println("testDeleteAll:");
        try {
            ResultSet resultSet = this.getTableData("DB1", "table1");
            assertEquals(4, resultSet.getHeaders().size());
            assertEquals(6, resultSet.getRows().size());

            sqlServer.executeStatement(SqlQueryFactory.delete("DB1", "table1"));

            resultSet = this.getTableData("DB1", "table1");
            assertEquals(4, resultSet.getHeaders().size());
            assertEquals(0, resultSet.getRows().size());

        } catch (SqlException se) {
            System.out.println(se.getMessage());
            fail();
        }
    }

    @Test
    public void testDeletePredicateIsNull() {

        System.out.println("testDeletePredicateIsNull:");
        try {
            ResultSet resultSet = this.getTableData("DB1", "table1");
            assertEquals(4, resultSet.getHeaders().size());
            assertEquals(6, resultSet.getRows().size());

            sqlServer.executeStatement(SqlQueryFactory.delete("DB1", "table1",
                    PredicateFactory.isNull(ColumnExprFactory.columnRef("column4"))));

            resultSet = this.getTableData("DB1", "table1");
            assertEquals(4, resultSet.getHeaders().size());
            assertEquals(3, resultSet.getRows().size());
            checkRowExists(resultSet, 10, 30, "test1", "t21");
            checkRowExists(resultSet, 12, 32, "test3", "t43");
            checkRowExists(resultSet, 13, 33, "test2", "t653");

        } catch (SqlException se) {
            System.out.println(se.getMessage());
            fail();
        }
    }

    @Test
    public void testDeletePredicateIsNotNull() {

        System.out.println("testDeletePredicateIsNotNull:");
        try {
            ResultSet resultSet = this.getTableData("DB1", "table1");
            assertEquals(4, resultSet.getHeaders().size());
            assertEquals(6, resultSet.getRows().size());

            sqlServer.executeStatement(SqlQueryFactory.delete("DB1", "table1",
                    PredicateFactory.isNotNull(ColumnExprFactory.columnRef("column4"))));

            resultSet = this.getTableData("DB1", "table1");
            assertEquals(4, resultSet.getHeaders().size());
            assertEquals(3, resultSet.getRows().size());
            checkRowExists(resultSet, 11, 31, "test2", null);
            checkRowExists(resultSet, 15, 34, "test1", null);
            checkRowExists(resultSet, 16, null, "test1", null);

        } catch (SqlException se) {
            System.out.println(se.getMessage());
            fail();
        }
    }
}
