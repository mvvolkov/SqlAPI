package testLocalFileDatabase;

import clientImpl.columnExpr.ColumnExprFactory;
import clientImpl.predicates.PredicateFactory;
import clientImpl.queries.QueryFactory;
import clientImpl.tables.TableRefFactory;
import org.junit.Test;
import sqlapi.exceptions.*;
import sqlapi.metadata.ColumnConstraintType;
import sqlapi.queryResult.ResultSet;

import java.util.Arrays;
import java.util.stream.Collectors;

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
public class InsertTest extends AbstractLocalFileDatabaseTest {


    /**
     * INSERT INTO DB1.table1 VALUES (10, 42, 'test');
     * <p>
     * Constraint violation for the column MySchema.table1.column1: PRIMARY KEY
     */
    @Test
    public void testPrimaryKeyConstraint() {
        System.out.println("testPrimaryKeyConstraint:");
        try {
            sqlServer.executeQuery(QueryFactory.insert(databaseName, "table1",
                    ColumnExprFactory.values(10, 42, "test")));
        } catch (ConstraintViolationException ce) {
            System.out.println(ce.getMessage());
            assertEquals("column1", ce.getColumnName());
            assertEquals(ColumnConstraintType.PRIMARY_KEY, ce.getConstraintType());
            return;
        } catch (SqlException se) {
            System.out.println(se.getMessage());
        }
        fail();
    }

    /**
     * INSERT INTO DB1.table1 VALUES (21, 43, NULL);
     * <p>
     * Constraint violation for the column MySchema.table1.column3: NOT NULL
     */
    @Test
    public void testNotNullConstraint() {
        System.out.println("testNotNullConstraint:");
        try {
            sqlServer.executeQuery(QueryFactory.insert("DB1", "table1",
                    ColumnExprFactory.values(21, 43, null)));
        } catch (ConstraintViolationException ce) {
            System.out.println(ce.getMessage());
            assertEquals("column3", ce.getColumnName());
            assertEquals(ColumnConstraintType.NOT_NULL, ce.getConstraintType());
            return;
        } catch (SqlException se) {
            System.out.println(se.getMessage());
        }
        fail();
    }


    /**
     * INSERT INTO DB1.table1(column2, column3, column1) VALUES (35, 'test15', 17);
     * SELECT * FROM DB1.table1;
     * <p>
     * column1, column2, column3, column4
     * 10, 30, test1, t21
     * 11, 31, test2, null
     * 12, 32, test3, t43
     * 13, 33, test2, t653
     * 15, 34, test1, null
     * 16, null, test1, null
     * 17, 35, test15, null
     */
    @Test
    public void testInsertColumns() {
        System.out.println("testInsertColumns:");
        try {
            sqlServer.executeQuery(QueryFactory.insert("DB1", "table1",
                    Arrays.asList("column2", "column3", "column1"),
                    ColumnExprFactory.values(35, "test15", 17)));
            ResultSet resultSet = this.getTableData("DB1", "table1");
            checkRowExists(resultSet, 17, 35, "test15", null);
        } catch (SqlException se) {
            System.out.println(se.getMessage());
            fail();
        }
    }

    /**
     * INSERT INTO DB1.table1 VALUES (17, 35, 39, NULL);
     * <p>
     * Wrong value type for the column dbo.table1.column3; Expected: String; Actual: Integer
     */
    @Test
    public void testInsertWrongType() {
        System.out.println("testInsertWrongType:");
        try {
            sqlServer.executeQuery(QueryFactory.insert("DB1", "table1",
                    ColumnExprFactory.values(17, 35, 39, null)));
        } catch (WrongValueTypeException wvte) {
            System.out.println(wvte.getMessage());
            assertEquals("table1", wvte.getTableName());
            assertEquals("column3", wvte.getColumnName());
            assertEquals("String",
                    wvte.getAllowedTypes().stream().map(Class::getSimpleName)
                            .collect(Collectors.joining(", ")));
            assertEquals("Integer", wvte.getActualType().getSimpleName());
            return;
        } catch (SqlException se) {
            System.out.println(se.getMessage());
        }
        fail();
    }

    /**
     * INSERT INTO DB1.table1(column2, column3, column1) VALUES ('test15', 17);
     * <p>
     * Invalid insert query. Number of values differs from number of columns
     */
    @Test
    public void testInsertWrongNumberOfValues() {
        System.out.println("testInsertWrongNumberOfValues:");
        try {
            sqlServer.executeQuery(QueryFactory.insert("DB1", "table1",
                    Arrays.asList("column2", "column3", "column1"),
                    ColumnExprFactory.values("test15", 17)));

        } catch (InvalidQueryException iqe) {
            System.out.println(iqe.getMessage());
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
     * <p>
     * INSERT INTO DB1.table1(column3, column1) VALUES ('test15', 17);
     * SELECT * FROM DB1.table1;
     * <p>
     * column1, column2, column3, column4
     * 10, 30, test1, t21
     * 11, 31, test2, null
     * 12, 32, test3, t43
     * 13, 33, test2, t653
     * 15, 34, test1, null
     * 16, null, test1, null
     * 17, 15, test15, null
     */
    @Test
    public void testInsertDefaultValue() {
        System.out.println("testInsertDefaultValue:");
        try {
            sqlServer.executeQuery(QueryFactory.insert("DB1", "table1",
                    Arrays.asList("column3", "column1"),
                    ColumnExprFactory.values("test15", 17)));

            ResultSet resultSet = this.getTableData("DB1", "table1");
            checkRowExists(resultSet, 17, 15, "test15", null);

        } catch (SqlException se) {
            System.out.println(se.getMessage());
            fail();
        }
    }

    /**
     * INSERT INTO DB1.table1(column3, column1, column4) VALUES ('test15', 17, 't12345');
     * <p>
     * Constraint violation for the column MySchema.table1.column4: SIZE EXCEEDED
     */
    @Test
    public void insertTooLongVarchar() {
        System.out.println("insertTooLongVarchar:");
        try {
            sqlServer.executeQuery(QueryFactory.insert("DB1", "table1",
                    Arrays.asList("column3", "column1", "column4"),
                    ColumnExprFactory.values("test15", 17, "t12345")));
        } catch (ConstraintViolationException ce) {
            System.out.println(ce.getMessage());
            assertEquals("column4", ce.getColumnName());
            assertEquals(ColumnConstraintType.MAX_SIZE, ce.getConstraintType());
            return;
        } catch (SqlException se) {
            System.out.println(se.getMessage());
        }
        fail();
    }


    /**
     * INSERT INTO DB1.table2 SELECT column2, column3 FROM DB1.table1 WHERE column4 IS NOT NULL;
     * SELECT * FROM DB1.table2;
     * <p>
     * column5, column3
     * 22, test3
     * 23, test2
     * 25, test4
     * 30, test1
     * 32, test3
     * 33, test2
     */
    @Test
    public void testInsertFromSelect() {
        System.out.println("testInsertFromSelect:");
        try {
            sqlServer.executeQuery(QueryFactory.insert("DB1", "table2",
                    QueryFactory.select(TableRefFactory.dbTable("DB1", "table1"),
                            Arrays.asList(ColumnExprFactory.columnRef("column2"),
                                    ColumnExprFactory.columnRef("column3")),
                            PredicateFactory.isNotNull("column4"))));

            ResultSet resultSet = this.getTableData("DB1", "table2");
            assertEquals(2, resultSet.getHeaders().size());
            assertEquals(6, resultSet.getRows().size());

            // old values
            checkRowExists(resultSet, 22, "test3");
            checkRowExists(resultSet, 23, "test2");
            checkRowExists(resultSet, 25, "test4");

            // inserted values
            checkRowExists(resultSet, 30, "test1");
            checkRowExists(resultSet, 32, "test3");
            checkRowExists(resultSet, 33, "test2");

        } catch (SqlException se) {
            System.out.println(se.getMessage());
            fail();
        }
    }
}
