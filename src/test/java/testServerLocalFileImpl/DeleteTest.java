package testServerLocalFileImpl;

import api.exceptions.SqlException;
import api.selectResult.ResultSet;
import clientImpl.columnExpr.ColumnExprFactory;
import clientImpl.predicates.PredicateFactory;
import clientImpl.queries.SqlQueryFactory;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class DeleteTest extends AbstractServerLocalFileTest {

    @Test
    public void testDeleteAll() {

        System.out.println("testDeleteAll:");
        try {
            ResultSet resultSet = this.getTableData("DB1", "table1");
            assertEquals(4, resultSet.getColumns().size());
            assertEquals(6, resultSet.getRows().size());

            sqlServer.executeStatement(SqlQueryFactory.delete("DB1", "table1"));

            resultSet = this.getTableData("DB1", "table1");
            assertEquals(4, resultSet.getColumns().size());
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
            assertEquals(4, resultSet.getColumns().size());
            assertEquals(6, resultSet.getRows().size());

            sqlServer.executeStatement(SqlQueryFactory.delete("DB1", "table1",
                    PredicateFactory.isNull(ColumnExprFactory.columnRef("column4"))));

            resultSet = this.getTableData("DB1", "table1");
            assertEquals(4, resultSet.getColumns().size());
            assertEquals(3, resultSet.getRows().size());

            Map<String, Object> values = new HashMap<>();
            values.put("column1", 10);
            values.put("column2", 30);
            values.put("column3", "test1");
            values.put("column4", "t21");
            checkRowExists(resultSet, values);

            values = new HashMap<>();
            values.put("column1", 12);
            values.put("column2", 32);
            values.put("column3", "test3");
            values.put("column4", "t43");
            checkRowExists(resultSet, values);

            values = new HashMap<>();
            values.put("column1", 13);
            values.put("column2", 33);
            values.put("column3", "test2");
            values.put("column4", "t653");
            checkRowExists(resultSet, values);

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
            assertEquals(4, resultSet.getColumns().size());
            assertEquals(6, resultSet.getRows().size());

            sqlServer.executeStatement(SqlQueryFactory.delete("DB1", "table1",
                    PredicateFactory.isNotNull(ColumnExprFactory.columnRef("column4"))));

            resultSet = this.getTableData("DB1", "table1");
            assertEquals(4, resultSet.getColumns().size());
            assertEquals(3, resultSet.getRows().size());


            Map<String, Object> values = new HashMap<>();
            values.put("column1", 11);
            values.put("column2", 31);
            values.put("column3", "test2");
            values.put("column4", null);
            checkRowExists(resultSet, values);

            values = new HashMap<>();
            values.put("column1", 15);
            values.put("column2", 34);
            values.put("column3", "test1");
            values.put("column4", null);
            checkRowExists(resultSet, values);

            values = new HashMap<>();
            values.put("column1", 16);
            values.put("column2", null);
            values.put("column3", "test1");
            values.put("column4", null);
            checkRowExists(resultSet, values);

        } catch (SqlException se) {
            System.out.println(se.getMessage());
            fail();
        }
    }
}
