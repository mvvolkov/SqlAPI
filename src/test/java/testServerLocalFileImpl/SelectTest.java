package testServerLocalFileImpl;

import clientImpl.columnExpr.ColumnExprFactory;
import clientImpl.metadata.MetadataFactory;
import clientImpl.predicates.PredicateFactory;
import clientImpl.queries.SqlQueryFactory;
import clientImpl.tableRef.TableRefFactory;
import org.junit.Test;
import sqlapi.exceptions.SqlException;
import sqlapi.misc.SelectedItem;
import sqlapi.selectResult.ResultSet;

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
 * <p>
 * SELECT * FROM DB1.table3;
 * <p>
 * column11, column12, column13
 * test3, cc, 41
 * ccc, t43, 38
 */
public class SelectTest extends AbstractServerLocalFileTest {

    @Override
    public void setUp() {
        super.setUp();

        try {
            // Create one more table
            sqlServer.executeQuery(SqlQueryFactory
                    .createTable("DB1", "table3", Arrays.asList(
                            MetadataFactory.varcharBuilder("column11", 20).notNull()
                                    .primaryKey().build(),
                            MetadataFactory.varcharBuilder("column12", 20).notNull()
                                    .defaultValue("default")
                                    .build(),
                            MetadataFactory.integerBuilder("column13")
                                    .notNull()
                                    .build()
                    )));

            // Fill table3
            sqlServer.executeQuery(SqlQueryFactory.insert("DB1", "table3",
                    Arrays.asList("test3", "cc", 41)));
            sqlServer.executeQuery(SqlQueryFactory.insert("DB1", "table3",
                    Arrays.asList("ccc", "t43", 38)));

            getTableData("DB1", "table3");


            System.out.println("===== END OF SET UP =====");

        } catch (SqlException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * SELECT * FROM DB1.table1;
     * <p>
     * column1, column2, column3, column4
     * 10, 30, test1, t21
     * 11, 31, test2, null
     * 12, 32, test3, t43
     * 13, 33, test2, t653
     * 15, 34, test1, null
     * 16, null, test1, null
     */
    @Test
    public void testSelectAll() {
        System.out.println("testSelectAll:");
        try {
            ResultSet resultSet = sqlServer.getQueryResult(
                    SqlQueryFactory.select(TableRefFactory.dbTable("DB1", "table1")));

            printResultSet(resultSet);

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
     * column2, column3, column1
     * 30, test1, 10
     * 31, test2, 11
     * 32, test3, 12
     * 33, test2, 13
     * 34, test1, 15
     * null, test1, 16
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

            printResultSet(resultSet);

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
     * One can select column expressions and values. It is possible to set alias for the
     * expression. If the column alias in not set the expression itself will be used as
     * a header.
     * <p>
     * SELECT 15, (column2 + column1) AS sum1, (column2 - 11) FROM DB1.table1 WHERE column2 IS NOT NULL;
     * <p>
     * N15, sum1, (column2 - 11)
     * 15, 40, 19
     * 15, 42, 20
     * 15, 44, 21
     * 15, 46, 22
     * 15, 49, 23
     */
    @Test
    public void testSelectColumnsExpressions() {
        System.out.println("testSelectColumnsExpressions:");
        try {
            ResultSet resultSet = sqlServer.getQueryResult(SqlQueryFactory.select(
                    TableRefFactory.dbTable("DB1", "table1"),
                    Arrays.asList(ColumnExprFactory.value(15, "N15"),
                            ColumnExprFactory.sumWithAlias("column2", "column1", "sum1"),
                            ColumnExprFactory.diff("column2", ColumnExprFactory.value(11))
                    ),
                    PredicateFactory.isNotNull("column2")
            ));

            printResultSet(resultSet);

            checkHeaders(resultSet.getHeaders(), "N15", "sum1", "(column2 - 11)");
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
     * One can select all columns from the table using tableName.*
     * It is possible to have same column names in the result set.
     * <p>
     * SELECT DB1.table2.*, column2, table1.column3, column1 FROM DB1.table1, DB1.table2
     * WHERE (column5 = 22) AND (column1 = 13);
     * <p>
     * column5, column3, column2, column3, column1
     * 22, test3, 33, test2, 13
     */
    @Test
    public void testSelectTableAndColumns() {
        System.out.println("testSelectAll:");
        try {
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
                    PredicateFactory.equals("column5", ColumnExprFactory.value(22))
                            .and(PredicateFactory
                                    .equals("column1", ColumnExprFactory.value(13)))
            ));

            printResultSet(resultSet);

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
     * Each table has column named column3. One can use column's full name to resolve
     * ambiguity.
     * <p>
     * SELECT column2, column1, table1.column3, column4, column5, table2.column3
     * FROM DB1.table2, DB1.table1
     * WHERE (table2.column3 = table1.column3) AND (column4 IS NOT NULL);
     * <p>
     * column2, column1, column3, column4, column5, column3
     * 32, 12, test3, t43, 22, test3
     * 33, 13, test2, t653, 23, test2
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
                            PredicateFactory.equals("table2", "column3", "table1", "column3")
                                    .and(PredicateFactory.isNotNull("column4"))));

            printResultSet(resultSet);

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
     * In this test using of column3 is ambiguous and leads to exception.
     * <p>
     * SELECT column2, column1, column3, column4, column5, table2.column3
     * FROM DB1.table2, DB1.table1
     * WHERE (table2.column3 = table1.column3) AND (column4 IS NOT NULL);
     * <p>
     * Ambiguous column name: column3
     */
    @Test
    public void testSelectFromTwoTablesSameColumnNameExists2() {

        System.out.println("testSelectFromTwoTables:");

        try {

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
                    PredicateFactory.equals("table2", "column3", "table1", "column3")
                            .and(PredicateFactory.isNotNull("column4"))));
        } catch (SqlException se) {
            System.out.println(se.getMessage());
            assertEquals("Ambiguous column name: column3", se.getMessage());
            return;
        }
        fail();
    }

    /**
     * 1.
     * SELECT * FROM DB1.table2 INNER JOIN DB1.table1 ON table2.column3 = table1.column3;
     * <p>
     * column5, column3, column1, column2, column3, column4
     * 22, test3, 12, 32, test3, t43
     * 23, test2, 11, 31, test2, null
     * 23, test2, 13, 33, test2, t653
     * <p>
     * <p>
     * 2.
     * SELECT column5, column1, column2, column4 FROM DB1.table2
     * INNER JOIN DB1.table1 ON table2.column3 = table1.column3 WHERE column4 = 't653';
     * <p>
     * column5, column1, column2, column4
     * 23, 13, 33, t653
     * <p>
     * <p>
     * 3.
     * SELECT * FROM DB1.table2 INNER JOIN DB1.table1 ON (column2 IS NOT NULL) AND (column5 = (column2 - 9));
     * <p>
     * column5, column3, column1, column2, column3, column4
     * 22, test3, 11, 31, test2, null
     * 23, test2, 12, 32, test3, t43
     * 25, test4, 15, 34, test1, null
     * <p>
     * <p>
     * 4.
     * SELECT * FROM (DB1.table2 INNER JOIN DB1.table1 ON (column2 IS NOT NULL)
     * AND (column5 = (column2 - 9)))
     * INNER JOIN DB1.table3 ON (table2.column3 = table3.column11) OR (column4 = column12);
     * <p>
     * column5, column3, column1, column2, column3, column4, column11, column12, column13
     * 22, test3, 11, 31, test2, null, test3, cc, 41
     * 23, test2, 12, 32, test3, t43, ccc, t43, 38
     */
    @Test
    public void testInnerJoin() {

        try {

            // 1.
            ResultSet resultSet = sqlServer.getQueryResult(SqlQueryFactory.select(
                    TableRefFactory.innerJoin("DB1", "table2", "DB1", "table1",
                            PredicateFactory.equals("table2", "column3", "table1", "column3"))));

            printResultSet(resultSet);

            checkHeaders(resultSet.getHeaders(), "column5", "column3", "column1",
                    "column2", "column3", "column4");
            assertEquals(3, resultSet.getRows().size());
            checkRowExists(resultSet, 22, "test3", 12, 32, "test3", "t43");
            checkRowExists(resultSet, 23, "test2", 11, 31, "test2", null);
            checkRowExists(resultSet, 23, "test2", 13, 33, "test2", "t653");


            // 2.
            resultSet = sqlServer.getQueryResult(SqlQueryFactory.select(
                    TableRefFactory.innerJoin("DB1", "table2", "DB1", "table1",
                            PredicateFactory.equals("table2", "column3", "table1", "column3")),
                    Arrays.asList(
                            ColumnExprFactory.columnRef("column5"),
                            ColumnExprFactory.columnRef("column1"),
                            ColumnExprFactory.columnRef("column2"),
                            ColumnExprFactory.columnRef("column4"))
                    ,
                    PredicateFactory.equals("column4", ColumnExprFactory.value("t653"))));

            printResultSet(resultSet);

            checkHeaders(resultSet.getHeaders(), "column5", "column1", "column2", "column4");
            assertEquals(1, resultSet.getRows().size());
            checkRowExists(resultSet, 23, 13, 33, "t653");

            // 3.
            resultSet = sqlServer.getQueryResult(SqlQueryFactory.select(
                    TableRefFactory.innerJoin("DB1", "table2", "DB1", "table1",
                            PredicateFactory.isNotNull("column2").and(
                                    PredicateFactory.equals("column5",
                                            ColumnExprFactory.diff("column2", ColumnExprFactory.value(9))
                                    )))));

            printResultSet(resultSet);

            checkHeaders(resultSet.getHeaders(), "column5", "column3", "column1",
                    "column2", "column3", "column4");
            assertEquals(3, resultSet.getRows().size());
            checkRowExists(resultSet, 22, "test3", 11, 31, "test2", null);
            checkRowExists(resultSet, 23, "test2", 12, 32, "test3", "t43");
            checkRowExists(resultSet, 25, "test4", 15, 34, "test1", null);

            // 4.
            resultSet = sqlServer.getQueryResult(SqlQueryFactory.select(
                    TableRefFactory.innerJoin(
                            TableRefFactory.innerJoin("DB1", "table2", "DB1", "table1",
                                    PredicateFactory.isNotNull("column2").and(
                                            PredicateFactory.equals("column5",
                                                    ColumnExprFactory.diff("column2", ColumnExprFactory.value(9))
                                            ))),
                            "DB1", "table3",
                            PredicateFactory.equals("table2", "column3", "table3", "column11").or(
                                    PredicateFactory.equals("column4", "column12")
                            )
                    )));

            printResultSet(resultSet);
            checkHeaders(resultSet.getHeaders(), "column5", "column3", "column1",
                    "column2", "column3", "column4", "column11", "column12", "column13");
            assertEquals(2, resultSet.getRows().size());
            checkRowExists(resultSet, 22, "test3", 11, 31, "test2", null, "test3", "cc", 41);
            checkRowExists(resultSet, 23, "test2", 12, 32, "test3", "t43", "ccc", "t43", 38);

        } catch (SqlException se) {
            System.out.println(se.getMessage());
            fail();
        }
    }

    /**
     * SELECT * FROM DB1.table2 LEFT OUTER JOIN DB1.table3 ON table2.column3 = table3.column11;
     * <p>
     * column5, column3, column11, column12, column13
     * 22, test3, test3, cc, 41
     * 23, test2, null, null, null
     * 25, test4, null, null, null
     */
    @Test
    public void testLeftOuterJoin() {
        try {
            ResultSet resultSet = sqlServer.getQueryResult(SqlQueryFactory.select(
                    TableRefFactory.leftOuterJoin("DB1", "table2", "DB1", "table3",
                            PredicateFactory.equals("table2", "column3", "table3", "column11"))));

            printResultSet(resultSet);

            checkHeaders(resultSet.getHeaders(), "column5", "column3", "column11",
                    "column12", "column13");
            assertEquals(3, resultSet.getRows().size());
            checkRowExists(resultSet, 22, "test3", "test3", "cc", 41);
            checkRowExists(resultSet, 23, "test2", null, null, null);
            checkRowExists(resultSet, 25, "test4", null, null, null);
        } catch (SqlException se) {
            System.out.println(se.getMessage());
            fail();
        }
    }

    /**
     * SELECT * FROM DB1.table1 RIGHT OUTER JOIN DB1.table3 ON table1.column4 = table3.column12;
     * <p>
     * column1, column2, column3, column4, column11, column12, column13
     * null, null, null, null, test3, cc, 41
     * 12, 32, test3, t43, ccc, t43, 38
     */
    @Test
    public void testRightOuterJoin() {
        try {
            ResultSet resultSet = sqlServer.getQueryResult(SqlQueryFactory.select(
                    TableRefFactory.rightOuterJoin("DB1", "table1", "DB1", "table3",
                            PredicateFactory.equals("table1", "column4", "table3", "column12"))));

            printResultSet(resultSet);

            checkHeaders(resultSet.getHeaders(), "column1", "column2", "column3", "column4",
                    "column11", "column12", "column13");
            assertEquals(2, resultSet.getRows().size());
            checkRowExists(resultSet, null, null, null, null, "test3", "cc", 41);
            checkRowExists(resultSet, 12, 32, "test3", "t43", "ccc", "t43", 38);
        } catch (SqlException se) {
            System.out.println(se.getMessage());
            fail();
        }
    }

    /**
     * SELECT * FROM DB1.table2, (SELECT column2, column3 FROM DB1.table1) t1
     * WHERE table2.column3 = t1.column3;
     * <p>
     * column5, column3, column2, column3
     * 22, test3, 32, test3
     * 23, test2, 31, test2
     * 23, test2, 33, test2
     */
    @Test
    public void testSelectFromSubquery() {

        try {
            ResultSet resultSet = sqlServer.getQueryResult(SqlQueryFactory.select(
                    Arrays.asList(TableRefFactory.dbTable("DB1", "table2"),
                            TableRefFactory.tableFromSelect(SqlQueryFactory.select(
                                    TableRefFactory.dbTable("DB1", "table1"), Arrays.asList(
                                            ColumnExprFactory.columnRef("column2"),
                                            ColumnExprFactory.columnRef("column3")
                                    )), "t1")
                    ),
                    PredicateFactory
                            .equals(ColumnExprFactory.columnRef("table2", "column3"),
                                    ColumnExprFactory.columnRef("t1", "column3"))));

            printResultSet(resultSet);

            checkHeaders(resultSet.getHeaders(), "column5", "column3", "column2", "column3");
            assertEquals(3, resultSet.getRows().size());
            checkRowExists(resultSet, 22, "test3", 32, "test3");
            checkRowExists(resultSet, 23, "test2", 31, "test2");
            checkRowExists(resultSet, 23, "test2", 33, "test2");


        } catch (SqlException se) {
            System.out.println(se.getMessage());
            fail();
        }

    }

    /**
     * SELECT column3, (COUNT(*) + 1) AS COUNT ALL + 1, COUNT(column2) AS COUNT column2
     * FROM DB1.table1 GROUP BY column3;
     * <p>
     * column3, COUNT ALL + 1, COUNT column2
     * test3, 2, 1
     * test1, 4, 2
     * test2, 3, 2
     */
    @Test
    public void testGroupByCount() {


        try {
            ResultSet resultSet = sqlServer.getQueryResult(SqlQueryFactory.selectGrouped(
                    TableRefFactory.dbTable("DB1", "table1"),
                    Arrays.asList(ColumnExprFactory.columnRef("column3"),
                            ColumnExprFactory.sumWithAlias(ColumnExprFactory.countAll(),
                                    ColumnExprFactory.value(1), "COUNT ALL + 1"),
                            ColumnExprFactory.countWithAlias("column2", "COUNT column2")),
                    Collections
                            .singletonList(ColumnExprFactory.columnRef("column3"))));

            printResultSet(resultSet);
            checkHeaders(resultSet.getHeaders(), "column3", "COUNT ALL + 1", "COUNT column2");
            assertEquals(3, resultSet.getRows().size());
            checkRowExists(resultSet, "test3", 2, 1);
            checkRowExists(resultSet, "test1", 4, 2);
            checkRowExists(resultSet, "test2", 3, 2);

        } catch (SqlException se) {
            System.out.println(se.getMessage());
            fail();
        }

    }

    /**
     * SELECT column3, SUM(column1), SUM(column2) AS S2 FROM DB1.table1
     * WHERE (column1 IS NOT NULL) AND (column2 IS NOT NULL) GROUP BY column3;
     * <p>
     * column3, SUM(column1), S2
     * test3, 12, 32
     * test1, 25, 64
     * test2, 24, 64
     */
    @Test
    public void testGroupBySum() {
        try {
            ResultSet resultSet = sqlServer.getQueryResult(SqlQueryFactory.selectGrouped(
                    TableRefFactory.dbTable("DB1", "table1"),
                    Arrays.asList(ColumnExprFactory.columnRef("column3"),
                            ColumnExprFactory.groupSum("column1"),
                            ColumnExprFactory.groupSumWithAlias("column2", "S2")),
                    PredicateFactory.isNotNull("column1").and(PredicateFactory.isNotNull("column2")),
                    Collections.singletonList(ColumnExprFactory.columnRef("column3"))));

            printResultSet(resultSet);


        } catch (SqlException se) {
            System.out.println(se.getMessage());
            fail();
        }
    }

    /**
     * SELECT column3, SUM(column1) AS SUM1, MAX(column1) AS MAX1, MIN(column1) AS MIN1,
     * SUM(column2) AS SUM2, MAX(column2) AS MAX2, MIN(column2) AS MIN2 FROM DB1.table1
     * WHERE (column1 IS NOT NULL) AND (column2 IS NOT NULL) GROUP BY column3;
     * <p>
     * column3, SUM1, MAX1, MIN1, SUM2, MAX2, MIN2
     * test3, 12, 12, 12, 32, 32, 32
     * test1, 25, 15, 10, 64, 34, 30
     * test2, 24, 13, 11, 64, 33, 31
     */
    @Test
    public void testGroupBySumMaxMin() {
        try {
            ResultSet resultSet = sqlServer.getQueryResult(SqlQueryFactory.selectGrouped(
                    TableRefFactory.dbTable("DB1", "table1"),
                    Arrays.asList(ColumnExprFactory.columnRef("column3"),
                            ColumnExprFactory.groupSumWithAlias("column1", "SUM1"),
                            ColumnExprFactory.groupMaxWithAlias("column1", "MAX1"),
                            ColumnExprFactory.groupMinWithAlias("column1", "MIN1"),
                            ColumnExprFactory.groupSumWithAlias("column2", "SUM2"),
                            ColumnExprFactory.groupMaxWithAlias("column2", "MAX2"),
                            ColumnExprFactory.groupMinWithAlias("column2", "MIN2")),

                    PredicateFactory.isNotNull("column1").and(PredicateFactory.isNotNull("column2")),
                    Collections.singletonList(ColumnExprFactory.columnRef("column3"))));

            printResultSet(resultSet);
            checkHeaders(resultSet.getHeaders(), "column3", "SUM1", "MAX1", "MIN1", "SUM2", "MAX2", "MIN2");
            assertEquals(3, resultSet.getRows().size());
            checkRowExists(resultSet, "test3", 12, 12, 12, 32, 32, 32);
            checkRowExists(resultSet, "test1", 25, 15, 10, 64, 34, 30);
            checkRowExists(resultSet, "test2", 24, 13, 11, 64, 33, 31);

            printResultSet(resultSet);
        } catch (SqlException se) {
            System.out.println(se.getMessage());
            fail();
        }
    }


}
