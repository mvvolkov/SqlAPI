package testServerLocalFileImpl;

import api.exceptions.ConstraintException;
import api.exceptions.InvalidQueryException;
import api.exceptions.SqlException;
import api.exceptions.WrongValueTypeException;
import api.selectResult.ResultSet;
import clientImpl.columnExpr.ColumnExprFactory;
import clientImpl.predicates.PredicateFactory;
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
    public void testPrimaryKeyConstraint() {
        System.out.println("testPrimaryKeyConstraint:");
        try {
            sqlServer.executeStatement(SqlQueryFactory.insert("DB1", "table1",
                    Arrays.asList(10, 42, "test")));
        } catch (ConstraintException ce) {
            assertEquals("column1", ce.getColumnName());
            assertEquals("PRIMARY KEY", ce.getReason());
            return;
        } catch (SqlException se) {
            System.out.println(se.getMessage());
        }
        fail();
    }

    @Test
    public void testNotNullConstraint() {
        System.out.println("testNotNullConstraint:");
        try {
            sqlServer.executeStatement(SqlQueryFactory.insert("DB1", "table1",
                    Arrays.asList(21, 43, null)));
        } catch (ConstraintException ce) {
            assertEquals("column3", ce.getColumnName());
            assertEquals("NOT NULL", ce.getReason());
            return;
        } catch (SqlException se) {
            System.out.println(se.getMessage());
        }
        fail();
    }


    @Test
    public void testInsertColumns() {
        System.out.println("testInsertColumns:");
        try {
            sqlServer.executeStatement(SqlQueryFactory.insert("DB1", "table1",
                    Arrays.asList("column2", "column3", "column1"),
                    Arrays.asList(35, "test15", 17)));

            ResultSet resultSet = this.getTableData("DB1", "table1");
            Map<String, Object> values = new HashMap<>();
            values.put("column1", 17);
            values.put("column2", 35);
            values.put("column3", "test15");
            checkRowExists(resultSet, values);

        } catch (SqlException se) {
            System.out.println(se.getMessage());
            fail();
        }
    }

    @Test
    public void testInsertWrongType() {
        System.out.println("testInsertWrongType:");
        try {
            sqlServer.executeStatement(SqlQueryFactory.insert("DB1", "table1",
                    Arrays.asList(17, 35, 39, null)));
        } catch (WrongValueTypeException wvte) {
            assertEquals("table1", wvte.getColumnRef().getTableName());
            assertEquals("column3", wvte.getColumnRef().getColumnName());
            assertEquals("String", wvte.getExpectedClass().getSimpleName());
            assertEquals("Integer", wvte.getActualClass().getSimpleName());
            return;
        } catch (SqlException se) {
            System.out.println(se.getMessage());
        }
        fail();
    }

    /**
     * Check that exception is thrown when the number of columns differs from the number of values.
     */
    @Test
    public void testInsertWrongNumberOfValues() {
        System.out.println("testInsertWrongNumberOfValues:");
        try {
            sqlServer.executeStatement(SqlQueryFactory.insert("DB1", "table1",
                    Arrays.asList("column2", "column3", "column1"),
                    Arrays.asList("test15", 17)));

        } catch (InvalidQueryException iqe) {
            assertEquals(
                    "Invalid insert query. Number of values differs from number of columns",
                    iqe.getMessage());
            return;
        } catch (SqlException se) {
            System.out.println(se.getMessage());
        }
        fail();
    }

    /**
     * Check that default value is set for the column if its value is not specified.
     * Column "column2" has default value 15. Column "column4" has no default value.
     * Null is inserted instead.
     */
    @Test
    public void testInsertDefaultValue() {
        System.out.println("testInsertDefaultValue:");
        try {
            sqlServer.executeStatement(SqlQueryFactory.insert("DB1", "table1",
                    Arrays.asList("column3", "column1"),
                    Arrays.asList("test15", 17)));

            ResultSet resultSet = this.getTableData("DB1", "table1");
            Map<String, Object> values = new HashMap<>();
            values.put("column1", 17);
            values.put("column2", 15);
            values.put("column3", "test15");
            values.put("column4", null);
            checkRowExists(resultSet, values);

        } catch (SqlException se) {
            System.out.println(se.getMessage());
            fail();
        }
    }

    @Test
    public void insertTooLongVarchar() {
        System.out.println("insertTooLongVarchar:");
        try {
            sqlServer.executeStatement(SqlQueryFactory.insert("DB1", "table1",
                    Arrays.asList("column3", "column1", "column4"),
                    Arrays.asList("test15", 17, "t12345")));
        } catch (ConstraintException ce) {
            assertEquals("column4", ce.getColumnName());
            assertEquals("SIZE EXCEEDED", ce.getReason());
            return;
        } catch (SqlException se) {
            System.out.println(se.getMessage());
        }
        fail();
    }


    @Test
    public void testInsertFromSelect() {
        System.out.println("testInsertFromSelect:");
        try {
            sqlServer.executeStatement(SqlQueryFactory.insert("DB1", "table2",
                    SqlQueryFactory.select(TableRefFactory.dbTable("DB1", "table1"),
                            Arrays.asList(ColumnExprFactory.columnRef("column2"),
                                    ColumnExprFactory.columnRef("column3")),
                            PredicateFactory
                                    .isNotNull(ColumnExprFactory.columnRef("column4"
                                    )))));

            ResultSet resultSet = this.getTableData("DB1", "table2");
            assertEquals(2, resultSet.getColumns().size());
            assertEquals(6, resultSet.getRows().size());


            // old values
            Map<String, Object> values = new HashMap<>();
            values.put("column5", 22);
            values.put("column3", "test3");
            checkRowExists(resultSet, values);

            values = new HashMap<>();
            values.put("column5", 23);
            values.put("column3", "test2");
            checkRowExists(resultSet, values);

            values = new HashMap<>();
            values.put("column5", 25);
            values.put("column3", "test4");
            checkRowExists(resultSet, values);

            // inserted values
            values = new HashMap<>();
            values.put("column5", 30);
            values.put("column3", "test1");
            checkRowExists(resultSet, values);

            values = new HashMap<>();
            values.put("column5", 32);
            values.put("column3", "test3");
            checkRowExists(resultSet, values);

            values = new HashMap<>();
            values.put("column5", 33);
            values.put("column3", "test2");
            checkRowExists(resultSet, values);

        } catch (SqlException se) {
            System.out.println(se.getMessage());
            fail();
        }
    }

}
