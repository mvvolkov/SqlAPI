package testServerLocalFileImpl;

import sqlapi.exceptions.SqlException;
import sqlapi.misc.SelectedItem;
import sqlapi.selectResult.ResultSet;
import clientImpl.columnExpr.ColumnExprFactory;
import clientImpl.predicates.PredicateFactory;
import clientImpl.queries.SqlQueryFactory;
import clientImpl.tableRef.TableRefFactory;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class SelectTest extends AbstractServerLocalFileTest {

    /**
     * SELECT * FROM DB1.table1;
     * <p>
     * one can select all columns.
     */
    @Test
    public void testSelectAll() {
        System.out.println("testSelectAll:");
        try {
            ResultSet resultSet = sqlServer.getQueryResult(
                    SqlQueryFactory.select(TableRefFactory.dbTable("DB1", "table1")));

            checkHeaders(resultSet.getHeaders(), "column1", "column2", "column3",
                    "column4");
            assertEquals(6, resultSet.getRows().size());
            checkRowExists(resultSet, 10, 30, "test1", "t21");
            checkRowExists(resultSet, 11, 31, "test2", null);
            checkRowExists(resultSet, 12, 32, "test3", "t43");
            checkRowExists(resultSet, 13, 33, "test2", "t653");
            checkRowExists(resultSet, 15, 34, "test1", null);
            checkRowExists(resultSet, 16, null, "test1", null);

        } catch (SqlException e) {
            System.out.println(e.getMessage());
            fail();
        }
    }

    /**
     * SELECT column2, column3, column1 FROM DB1.table1;
     * <p>
     * Columns can be selected in arbitrary order.
     */
    @Test
    public void testSelectColumns() {
        System.out.println("testSelectColumns:");
        try {
            ResultSet resultSet = sqlServer.getQueryResult(SqlQueryFactory.select(
                    TableRefFactory.dbTable("DB1", "table1"),
                    Arrays.asList(ColumnExprFactory.columnRef("column2"),
                            ColumnExprFactory.columnRef("column3"),
                            ColumnExprFactory.columnRef("column1"))));

            checkHeaders(resultSet.getHeaders(), "column2", "column3", "column1");
            assertEquals(6, resultSet.getRows().size());
            checkRowExists(resultSet, 30, "test1", 10);
            checkRowExists(resultSet, 31, "test2", 11);
            checkRowExists(resultSet, 32, "test3", 12);
            checkRowExists(resultSet, 33, "test2", 13);
            checkRowExists(resultSet, 34, "test1", 15);
            checkRowExists(resultSet, null, "test1", 16);

        } catch (SqlException e) {
            System.out.println(e.getMessage());
            fail();
        }
    }

    /**
     * SELECT 15, (column2 + column1) AS sum1, (column2 - 11) FROM DB1.table1 WHERE column2 IS NOT NULL;
     * <p>
     * One can use column expressions and values. It is possible to set alias for the
     * expression. If the column alias in not set the expression itself will be used as
     * a header.
     */
    @Test
    public void testSelectColumnsExpressions() {
        System.out.println("testSelectColumnsExpressions:");
        try {
            ResultSet resultSet = sqlServer.getQueryResult(SqlQueryFactory.select(
                    TableRefFactory.dbTable("DB1", "table1"),
                    Arrays.asList(ColumnExprFactory.value(15),
                            ColumnExprFactory.sum(ColumnExprFactory.columnRef("column2"),
                                    ColumnExprFactory.columnRef("column1"), "sum1"),
                            ColumnExprFactory.diff(ColumnExprFactory.columnRef("column2"),
                                    ColumnExprFactory.value(11))
                    ),
                    PredicateFactory.isNotNull(ColumnExprFactory.columnRef("column2"))
            ));

            checkHeaders(resultSet.getHeaders(), "15", "sum1", "(column2 - 11)");
            assertEquals(5, resultSet.getRows().size());
            checkRowExists(resultSet, 15, 40, 19);
            checkRowExists(resultSet, 15, 42, 20);
            checkRowExists(resultSet, 15, 44, 21);
            checkRowExists(resultSet, 15, 46, 22);
            checkRowExists(resultSet, 15, 49, 23);

        } catch (SqlException e) {
            System.out.println(e.getMessage());
            fail();
        }
    }

    /**
     * SELECT DB1.table2.*, column2, table1.column3, column1 FROM DB1.table1, DB1.table2
     * WHERE (column5 = 22) AND (column1 = 13);
     * <p>
     * One can select all columns from the table using tableName.*
     */
    @Test
    public void testSelectTableAndColumns() {
        System.out.println("testSelectAll:");
        try {
            // SELECT DB1.table2.*, column2, table1.column3, column1
            // FROM DB1.table1, DB1.table2 WHERE (column5 = 22) AND (column1 = 13);
            ResultSet resultSet = sqlServer.getQueryResult(SqlQueryFactory.select(
                    Arrays.asList(TableRefFactory.dbTable("DB1", "table1"),
                            TableRefFactory.dbTable("DB1", "table2")
                    ),
                    Arrays.asList(
                            (SelectedItem) TableRefFactory.dbTable("DB1", "table2"),
                            ColumnExprFactory.columnRef("column2"),
                            ColumnExprFactory.columnRef("table1", "column3"),
                            ColumnExprFactory.columnRef("column1")
                    ),
                    PredicateFactory.equals(ColumnExprFactory.columnRef("column5"),
                            ColumnExprFactory.value(22))
                            .and(PredicateFactory
                                    .equals(ColumnExprFactory.columnRef("column1"),
                                            ColumnExprFactory.value(13)))
            ));

            checkHeaders(resultSet.getHeaders(), "column5", "column3", "column2",
                    "column3", "column1");
            assertEquals(1, resultSet.getRows().size());
            checkRowExists(resultSet, 22, "test3", 33, "test2", 13);

        } catch (SqlException e) {
            System.out.println(e.getMessage());
            fail();
        }
    }


    /**
     * SELECT column2, column1, table1.column3, column4, column5, table2.column3
     * FROM DB1.table2, DB1.table1
     * WHERE (table2.column3 = table1.column3) AND (column4 IS NOT NULL);
     * <p>
     * Each table has column named column3. One can use column's full name to resolve
     * ambiguity.
     */
    @Test
    public void testSelectFromTwoTablesSameColumnNameExists1() {

        System.out.println("testSelectFromTwoTablesSameColumnNameExists1:");

        try {
            ResultSet resultSet =
                    sqlServer.getQueryResult(SqlQueryFactory.select(Arrays.asList(
                            TableRefFactory.dbTable("DB1", "table2"),
                            TableRefFactory.dbTable("DB1", "table1")),
                            Arrays.asList(
                                    ColumnExprFactory.columnRef("column2"),
                                    ColumnExprFactory.columnRef("column1"),
                                    ColumnExprFactory.columnRef("table1", "column3"),
                                    ColumnExprFactory.columnRef("column4"),
                                    ColumnExprFactory.columnRef("column5"),
                                    ColumnExprFactory.columnRef("table2", "column3")
                            ),
                            PredicateFactory
                                    .equals(ColumnExprFactory
                                                    .columnRef("table2", "column3"),
                                            ColumnExprFactory
                                                    .columnRef("table1", "column3"))
                                    .and(PredicateFactory
                                            .isNotNull(ColumnExprFactory
                                                    .columnRef("column4")))));

            checkHeaders(resultSet.getHeaders(), "column2", "column1", "column3",
                    "column4", "column5", "column3");
            assertEquals(2, resultSet.getRows().size());
            checkRowExists(resultSet, 32, 12, "test3", "t43", 22, "test3");
            checkRowExists(resultSet, 33, 13, "test2", "t653", 23, "test2");


        } catch (SqlException se) {
            System.out.println(se.getMessage());
            fail();
        }
    }

    /**
     * SELECT column2, column1, column3, column4, column5, table2.column3
     * FROM DB1.table2, DB1.table1
     * WHERE (table2.column3 = table1.column3) AND (column4 IS NOT NULL);
     * <p>
     * In this test using of column3 is ambiguous and leads to exception.
     */
    @Test
    public void testSelectFromTwoTablesSameColumnNameExists2() {

        System.out.println("testSelectFromTwoTables:");

        try {
            ResultSet resultSet =
                    sqlServer.getQueryResult(SqlQueryFactory.select(Arrays.asList(
                            TableRefFactory.dbTable("DB1", "table2"),
                            TableRefFactory.dbTable("DB1", "table1")),
                            Arrays.asList(
                                    ColumnExprFactory.columnRef("column2"),
                                    ColumnExprFactory.columnRef("column1"),
                                    ColumnExprFactory.columnRef("column3"),
                                    ColumnExprFactory.columnRef("column4"),
                                    ColumnExprFactory.columnRef("column5"),
                                    ColumnExprFactory.columnRef("table2", "column3")
                            ),
                            PredicateFactory
                                    .equals(ColumnExprFactory
                                                    .columnRef("table2", "column3"),
                                            ColumnExprFactory
                                                    .columnRef("table1", "column3"))
                                    .and(PredicateFactory
                                            .isNotNull(ColumnExprFactory
                                                    .columnRef("column4")))));
        } catch (SqlException se) {
            System.out.println(se.getMessage());
            assertEquals("Ambiguous column name: column3", se.getMessage());
            return;
        }
        fail();
    }

    @Test
    public void testInnerJoin() {

    }

    @Test
    public void testLeftOuterJoin() {

    }

    @Test
    public void testRightOuterJoin() {

    }

    @Test
    public void testGroupByAndCount() {

    }

    @Test
    public void testGroupByAndSum() {

    }

    @Test
    public void testGroupByAndMax() {

    }

    @Test
    public void testGroupByAndMin() {

    }
}
