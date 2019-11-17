package testServerLocalFileImpl;

import api.exceptions.ConstraintException;
import api.exceptions.InvalidQueryException;
import api.exceptions.SqlException;
import api.exceptions.WrongValueTypeException;
import api.selectResult.ResultSet;
import clientImpl.queries.SqlQueryFactory;
import clientImpl.tableRef.TableRefFactory;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class InsertTest extends AbstractServerLocalFileTest {

    @Test
    public void testInsert() {
        try {
            ResultSet resultSet = sqlServer.getQueryResult(
                    SqlQueryFactory.select(TableRefFactory.dbTable("DB1", "table1")));
            assertEquals(3, resultSet.getColumns().size());
            assertEquals(6, resultSet.getRows().size());

            Map<String, Object> keyValues = new HashMap<>();
            keyValues.put("column1", 10);
            Map<String, Object> otherValues = new HashMap<>();
            otherValues.put("column2", 30);
            otherValues.put("column3", "test1");
            checkRow(resultSet, keyValues, otherValues);

            keyValues = new HashMap<>();
            keyValues.put("column1", 11);
            otherValues = new HashMap<>();
            otherValues.put("column2", 31);
            otherValues.put("column3", "test2");
            checkRow(resultSet, keyValues, otherValues);

            keyValues = new HashMap<>();
            keyValues.put("column1", 12);
            otherValues = new HashMap<>();
            otherValues.put("column2", 32);
            otherValues.put("column3", "test3");
            checkRow(resultSet, keyValues, otherValues);

            keyValues = new HashMap<>();
            keyValues.put("column1", 13);
            otherValues = new HashMap<>();
            otherValues.put("column2", 33);
            otherValues.put("column3", "test2");
            checkRow(resultSet, keyValues, otherValues);

            keyValues = new HashMap<>();
            keyValues.put("column1", 15);
            otherValues = new HashMap<>();
            otherValues.put("column2", 34);
            otherValues.put("column3", "test1");
            checkRow(resultSet, keyValues, otherValues);

            keyValues = new HashMap<>();
            keyValues.put("column1", 16);
            otherValues = new HashMap<>();
            otherValues.put("column2", null);
            otherValues.put("column3", "test1");
            checkRow(resultSet, keyValues, otherValues);

        } catch (SqlException e) {
            System.out.println(e.getMessage());
            fail();
        }
    }


    @Test
    public void testPrimaryKeyConstraint() {
        try {
            sqlServer.executeStatement(SqlQueryFactory.insert("DB1", "table1",
                    Arrays.asList(10, 42, "test")));
        } catch (ConstraintException ce) {
            assertEquals("Constraint violation for the column dbo.table1.column1: " +
                    "PRIMARY KEY", ce.getMessage());
        } catch (SqlException se) {
            System.out.println(se.getMessage());
            fail();
        }
    }

    @Test
    public void testNotNullConstraint() {
        try {
            sqlServer.executeStatement(SqlQueryFactory.insert("DB1", "table1",
                    Arrays.asList(21, 43, null)));
        } catch (ConstraintException ce) {
            assertEquals(
                    "Constraint violation for the column dbo.table1.column3: NOT NULL",
                    ce.getMessage());
        } catch (SqlException se) {
            System.out.println(se.getMessage());
            fail();
        }
    }

    @Test
    public void testInsertColumns() {

        try {
            sqlServer.executeStatement(SqlQueryFactory.insert("DB1", "table1",
                    Arrays.asList("column2", "column3", "column1"),
                    Arrays.asList(35, "test15", 17)));

            ResultSet resultSet = sqlServer.getQueryResult(
                    SqlQueryFactory.select(TableRefFactory.dbTable("DB1", "table1")));


            Map<String, Object> keyValues = new HashMap<>();
            keyValues.put("column1", 17);
            Map<String, Object> otherValues = new HashMap<>();
            otherValues.put("column2", 35);
            otherValues.put("column3", "test15");
            checkRow(resultSet, keyValues, otherValues);

        } catch (SqlException se) {
            System.out.println(se.getMessage());
            fail();
        }
    }

    @Test
    public void testInsertWrongType() {
        try {
            sqlServer.executeStatement(SqlQueryFactory.insert("DB1", "table1",
                    Arrays.asList(17, 35, 39)));
        } catch (WrongValueTypeException wvte) {
            assertEquals("table1", wvte.getColumnRef().getTableName());
            assertEquals("column3", wvte.getColumnRef().getColumnName());
            assertEquals("String", wvte.getExpectedClass().getSimpleName());
            assertEquals("Integer", wvte.getActualClass().getSimpleName());
        } catch (SqlException se) {
            System.out.println(se.getMessage());
            fail();
        }
    }

    @Test
    public void testInsertWrongNumberOfValues() {
        try {
            sqlServer.executeStatement(SqlQueryFactory.insert("DB1", "table1",
                    Arrays.asList("column2", "column3", "column1"),
                    Arrays.asList("test15", 17)));

            ResultSet resultSet = sqlServer.getQueryResult(
                    SqlQueryFactory.select(TableRefFactory.dbTable("DB1", "table1")));


            Map<String, Object> keyValues = new HashMap<>();
            keyValues.put("column1", 17);
            Map<String, Object> otherValues = new HashMap<>();
            otherValues.put("column2", 35);
            otherValues.put("column3", "test15");
            checkRow(resultSet, keyValues, otherValues);

        } catch (InvalidQueryException iqe) {
            assertEquals(
                    "Invalid insert query. Number of values differs from number of columns",
                    iqe.getMessage());
        } catch (SqlException se) {
            System.out.println(se.getMessage());
            fail();
        }
    }

    /**
     * Column "column2" has default value 15.
     */
    @Test
    public void testInsertDefaultValue() {
        try {
            sqlServer.executeStatement(SqlQueryFactory.insert("DB1", "table1",
                    Arrays.asList("column3", "column1"),
                    Arrays.asList("test15", 17)));

            ResultSet resultSet = sqlServer.getQueryResult(
                    SqlQueryFactory.select(TableRefFactory.dbTable("DB1", "table1")));


            Map<String, Object> keyValues = new HashMap<>();
            keyValues.put("column1", 17);
            Map<String, Object> otherValues = new HashMap<>();
            otherValues.put("column2", 15);
            otherValues.put("column3", "test15");
            checkRow(resultSet, keyValues, otherValues);

        } catch (SqlException se) {
            System.out.println(se.getMessage());
            fail();
        }
    }

    @Test
    public void testInsertFromSelect() {
        // TODO
        fail();
    }

}
