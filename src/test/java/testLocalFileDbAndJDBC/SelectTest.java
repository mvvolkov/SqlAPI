package testLocalFileDbAndJDBC;

import clientImpl.columnExpr.ColumnExprFactory;
import clientImpl.metadata.MetadataFactory;
import clientImpl.predicates.PredicateFactory;
import clientImpl.queries.QueryFactory;
import clientImpl.tables.TableRefFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sqlapi.exceptions.SqlException;
import sqlapi.exceptions.WrappedException;
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
 * <p>
 * SELECT * FROM DB1.table3;
 * <p>
 * column11, column12, column13
 * test3, cc, 41
 * ccc, t43, 38
 */
public class SelectTest extends AbstractTestRunner {

    public SelectTest(SqlServer sqlServer, String database) {
        super(sqlServer, database);
    }


    @Before
    @Override
    public void setUp() {
        super.setUp();

        try {
            // Create one more table
            sqlServer.executeQuery(QueryFactory
                    .createTable(databaseName,
                            MetadataFactory.tableMetadata("table3", Arrays.asList(
                                    MetadataFactory.varchar("column11", 20, Collections
                                            .singletonList(MetadataFactory.primaryKey())),
                                    MetadataFactory.varchar("column12", 20,
                                            Arrays.asList(MetadataFactory.notNull(),
                                                    MetadataFactory
                                                            .defaultVal("default"))),
                                    MetadataFactory.integer("column13", Collections
                                            .singletonList(MetadataFactory.notNull()))
                            ))));

            // Fill table3
            sqlServer.executeQuery(QueryFactory.insert(databaseName, "table3",
                    ColumnExprFactory.values("test3", "cc", 41)));
            sqlServer.executeQuery(QueryFactory.insert(databaseName, "table3",
                    ColumnExprFactory.values("ccc", "t43", 38)));
            getTableData(databaseName, "table3");
            System.out.println("===== END OF SET UP =====");

        } catch (SqlException e) {
            System.out.println(e.getMessage());
        }
    }

    @After
    @Override
    public void tearDown() {
        try {
            sqlServer.executeQuery(QueryFactory.dropTable(databaseName, "table3"));
        } catch (SqlException e) {
            e.printStackTrace();
        }
        super.tearDown();
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
            QueryResult queryResult = sqlServer.getQueryResult(
                    QueryFactory.select(TableRefFactory.dbTable(databaseName, "table1")));

            printResultSet(queryResult);

            checkHeaders(queryResult.getHeaders(), "column1", "column2", "column3",
                    "column4");
            assertEquals(6, queryResult.getRows().size());
            checkRowExists(queryResult, 10, 30, "test1", "t21");
            checkRowExists(queryResult, 11, 31, "test2", null);
            checkRowExists(queryResult, 12, 32, "test3", "t43");
            checkRowExists(queryResult, 13, 33, "test2", "t653");
            checkRowExists(queryResult, 15, 34, "test1", null);
            checkRowExists(queryResult, 16, null, "test1", null);

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
            QueryResult queryResult = sqlServer.getQueryResult(QueryFactory.select(
                    TableRefFactory.dbTable(databaseName, "table1"),
                    Arrays.asList(ColumnExprFactory.columnRef("column2"),
                            ColumnExprFactory.columnRef("column3"),
                            ColumnExprFactory.columnRef("column1"))));

            printResultSet(queryResult);

            checkHeaders(queryResult.getHeaders(), "column2", "column3", "column1");
            assertEquals(6, queryResult.getRows().size());
            checkRowExists(queryResult, 30, "test1", 10);
            checkRowExists(queryResult, 31, "test2", 11);
            checkRowExists(queryResult, 32, "test3", 12);
            checkRowExists(queryResult, 33, "test2", 13);
            checkRowExists(queryResult, 34, "test1", 15);
            checkRowExists(queryResult, null, "test1", 16);

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
            QueryResult queryResult = sqlServer.getQueryResult(QueryFactory.select(
                    TableRefFactory.dbTable(databaseName, "table1"),
                    Arrays.asList(ColumnExprFactory.valueWithAlias(15, "N15"),
                            ColumnExprFactory.sumWithAlias("column2", "column1", "sum1"),
                            ColumnExprFactory.columnRef("column2")
                                    .subtract(ColumnExprFactory.value(11))
                    ),
                    PredicateFactory.isNotNull("column2")
            ));

            printResultSet(queryResult);

            checkHeaders(queryResult.getHeaders(), "N15", "sum1", "(column2 - 11)");
            assertEquals(5, queryResult.getRows().size());
            checkRowExists(queryResult, 15, 40, 19);
            checkRowExists(queryResult, 15, 42, 20);
            checkRowExists(queryResult, 15, 44, 21);
            checkRowExists(queryResult, 15, 46, 22);
            checkRowExists(queryResult, 15, 49, 23);

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
            QueryResult queryResult = sqlServer.getQueryResult(QueryFactory.select(
                    Arrays.asList(TableRefFactory.dbTable(databaseName, "table1"),
                            TableRefFactory.dbTable(databaseName, "table2")
                    ),
                    Arrays.asList(TableRefFactory.dbTable(databaseName, "table2"),
                            ColumnExprFactory.columnRef("column2"),
                            ColumnExprFactory.columnRef("table1", "column3"),
                            ColumnExprFactory.columnRef("column1")
                    ),
                    PredicateFactory
                            .equals("column5", ColumnExprFactory.value(22))
                            .and(PredicateFactory
                                    .equals("column1",
                                            ColumnExprFactory.value(13)))
            ));

            printResultSet(queryResult);

            checkHeaders(queryResult.getHeaders(), "column5", "column3", "column2",
                    "column3", "column1");
            assertEquals(1, queryResult.getRows().size());
            checkRowExists(queryResult, 22, "test3", 33, "test2", 13);

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
            QueryResult queryResult =
                    sqlServer.getQueryResult(QueryFactory.select(Arrays.asList(
                            TableRefFactory.dbTable(databaseName, "table2"),
                            TableRefFactory.dbTable(databaseName, "table1")),
                            Arrays.asList(
                                    ColumnExprFactory.columnRef("column2"),
                                    ColumnExprFactory.columnRef("column1"),
                                    ColumnExprFactory.columnRef("table1", "column3"),
                                    ColumnExprFactory.columnRef("column4"),
                                    ColumnExprFactory.columnRef("column5"),
                                    ColumnExprFactory.columnRef("table2", "column3")
                            ),
                            PredicateFactory
                                    .equals("table2", "column3", "table1", "column3")
                                    .and(PredicateFactory.isNotNull("column4"))));

            printResultSet(queryResult);

            checkHeaders(queryResult.getHeaders(), "column2", "column1", "column3",
                    "column4", "column5", "column3");
            assertEquals(2, queryResult.getRows().size());
            checkRowExists(queryResult, 32, 12, "test3", "t43", 22, "test3");
            checkRowExists(queryResult, 33, 13, "test2", "t653", 23, "test2");


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

            sqlServer.getQueryResult(QueryFactory.select(Arrays.asList(
                    TableRefFactory.dbTable(databaseName, "table2"),
                    TableRefFactory.dbTable(databaseName, "table1")),
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
        } catch (WrappedException we) {
            assertEquals("Column 'column3' in field list is ambiguous", we.getMessage());
            return;
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
            QueryResult queryResult = sqlServer.getQueryResult(QueryFactory.select(
                    TableRefFactory
                            .innerJoin(databaseName, "table2", databaseName, "table1",
                                    PredicateFactory
                                            .equals("table2", "column3", "table1",
                                                    "column3"))));

            printResultSet(queryResult);

            checkHeaders(queryResult.getHeaders(), "column5", "column3", "column1",
                    "column2", "column3", "column4");
            assertEquals(3, queryResult.getRows().size());
            checkRowExists(queryResult, 22, "test3", 12, 32, "test3", "t43");
            checkRowExists(queryResult, 23, "test2", 11, 31, "test2", null);
            checkRowExists(queryResult, 23, "test2", 13, 33, "test2", "t653");


            // 2.
            queryResult = sqlServer.getQueryResult(QueryFactory.select(
                    TableRefFactory
                            .innerJoin(databaseName, "table2", databaseName, "table1",
                                    PredicateFactory
                                            .equals("table2", "column3", "table1",
                                                    "column3")),
                    Arrays.asList(
                            ColumnExprFactory.columnRef("column5"),
                            ColumnExprFactory.columnRef("column1"),
                            ColumnExprFactory.columnRef("column2"),
                            ColumnExprFactory.columnRef("column4"))
                    ,
                    PredicateFactory.equals("column4",
                            ColumnExprFactory.value("t653"))));

            printResultSet(queryResult);

            checkHeaders(queryResult.getHeaders(), "column5", "column1", "column2",
                    "column4");
            assertEquals(1, queryResult.getRows().size());
            checkRowExists(queryResult, 23, 13, 33, "t653");

            // 3.
            queryResult = sqlServer.getQueryResult(QueryFactory.select(
                    TableRefFactory
                            .innerJoin(databaseName, "table2", databaseName, "table1",
                                    PredicateFactory.isNotNull("column2").and(
                                            PredicateFactory.equals("column5",
                                                    ColumnExprFactory.diff("column2",
                                                            ColumnExprFactory
                                                                    .value(9))
                                            )))));

            printResultSet(queryResult);

            checkHeaders(queryResult.getHeaders(), "column5", "column3", "column1",
                    "column2", "column3", "column4");
            assertEquals(3, queryResult.getRows().size());
            checkRowExists(queryResult, 22, "test3", 11, 31, "test2", null);
            checkRowExists(queryResult, 23, "test2", 12, 32, "test3", "t43");
            checkRowExists(queryResult, 25, "test4", 15, 34, "test1", null);

            // 4.
            queryResult = sqlServer.getQueryResult(QueryFactory.select(
                    TableRefFactory.innerJoin(
                            TableRefFactory
                                    .innerJoin(databaseName, "table2", databaseName,
                                            "table1",
                                            PredicateFactory.isNotNull("column2").and(
                                                    PredicateFactory.equals("column5",
                                                            ColumnExprFactory
                                                                    .diff("column2",
                                                                            ColumnExprFactory
                                                                                    .value(
                                                                                            9))
                                                    ))),
                            databaseName, "table3",
                            PredicateFactory
                                    .equals("table2", "column3", "table3", "column11").or(
                                    PredicateFactory.equals("column4", "column12")
                            )
                    )));

            printResultSet(queryResult);
            checkHeaders(queryResult.getHeaders(), "column5", "column3", "column1",
                    "column2", "column3", "column4", "column11", "column12", "column13");
            assertEquals(2, queryResult.getRows().size());
            checkRowExists(queryResult, 22, "test3", 11, 31, "test2", null, "test3", "cc",
                    41);
            checkRowExists(queryResult, 23, "test2", 12, 32, "test3", "t43", "ccc", "t43",
                    38);

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
            QueryResult queryResult = sqlServer.getQueryResult(QueryFactory.select(
                    TableRefFactory
                            .leftOuterJoin(databaseName, "table2", databaseName, "table3",
                                    PredicateFactory
                                            .equals("table2", "column3", "table3",
                                                    "column11"))));

            printResultSet(queryResult);

            checkHeaders(queryResult.getHeaders(), "column5", "column3", "column11",
                    "column12", "column13");
            assertEquals(3, queryResult.getRows().size());
            checkRowExists(queryResult, 22, "test3", "test3", "cc", 41);
            checkRowExists(queryResult, 23, "test2", null, null, null);
            checkRowExists(queryResult, 25, "test4", null, null, null);
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
            QueryResult queryResult = sqlServer.getQueryResult(QueryFactory.select(
                    TableRefFactory.rightOuterJoin(databaseName, "table1", databaseName,
                            "table3",
                            PredicateFactory
                                    .equals("table1", "column4", "table3", "column12"))));

            printResultSet(queryResult);

            checkHeaders(queryResult.getHeaders(), "column1", "column2", "column3",
                    "column4",
                    "column11", "column12", "column13");
            assertEquals(2, queryResult.getRows().size());
            checkRowExists(queryResult, null, null, null, null, "test3", "cc", 41);
            checkRowExists(queryResult, 12, 32, "test3", "t43", "ccc", "t43", 38);
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
            QueryResult queryResult = sqlServer.getQueryResult(QueryFactory.select(
                    Arrays.asList(TableRefFactory.dbTable(databaseName, "table2"),
                            TableRefFactory.tableFromSelect(QueryFactory.select(
                                    TableRefFactory.dbTable(databaseName, "table1"),
                                    Arrays.asList(
                                            ColumnExprFactory.columnRef("column2"),
                                            ColumnExprFactory.columnRef("column3")
                                    )), "t1")
                    ),
                    PredicateFactory
                            .equals(ColumnExprFactory.columnRef("table2", "column3"),
                                    ColumnExprFactory.columnRef("t1", "column3"))));

            printResultSet(queryResult);

            checkHeaders(queryResult.getHeaders(), "column5", "column3", "column2",
                    "column3");
            assertEquals(3, queryResult.getRows().size());
            checkRowExists(queryResult, 22, "test3", 32, "test3");
            checkRowExists(queryResult, 23, "test2", 31, "test2");
            checkRowExists(queryResult, 23, "test2", 33, "test2");


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
            QueryResult queryResult = sqlServer.getQueryResult(QueryFactory.selectGrouped(
                    TableRefFactory.dbTable(databaseName, "table1"),
                    Arrays.asList(ColumnExprFactory.columnRef("column3"),
                            ColumnExprFactory.sumWithAlias(ColumnExprFactory.countAll(),
                                    ColumnExprFactory.value(1),
                                    "COUNT_ALL_plus_1"),
                            ColumnExprFactory.countWithAlias("column2", "COUNT_column2")),
                    Collections
                            .singletonList(ColumnExprFactory.columnRef("column3"))));

            printResultSet(queryResult);
            checkHeaders(queryResult.getHeaders(), "column3", "COUNT_ALL_plus_1",
                    "COUNT_column2");
            assertEquals(3, queryResult.getRows().size());
            checkRowExists(queryResult, "test3", 2, 1);
            checkRowExists(queryResult, "test1", 4, 2);
            checkRowExists(queryResult, "test2", 3, 2);

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
            QueryResult queryResult = sqlServer.getQueryResult(QueryFactory.selectGrouped(
                    TableRefFactory.dbTable(databaseName, "table1"),
                    Arrays.asList(ColumnExprFactory.columnRef("column3"),
                            ColumnExprFactory.groupSum("column1"),
                            ColumnExprFactory.groupSumWithAlias("column2", "S2")),
                    PredicateFactory.isNotNull("column1")
                            .and(PredicateFactory.isNotNull("column2")),
                    Collections.singletonList(ColumnExprFactory.columnRef("column3"))));

            printResultSet(queryResult);


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
            QueryResult queryResult = sqlServer.getQueryResult(QueryFactory.selectGrouped(
                    TableRefFactory.dbTable(databaseName, "table1"),
                    Arrays.asList(ColumnExprFactory.columnRef("column3"),
                            ColumnExprFactory.groupSumWithAlias("column1", "SUM1"),
                            ColumnExprFactory.maxWithAlias("column1", "MAX1"),
                            ColumnExprFactory.minWithAlias("column1", "MIN1"),
                            ColumnExprFactory.groupSumWithAlias("column2", "SUM2"),
                            ColumnExprFactory.maxWithAlias("column2", "MAX2"),
                            ColumnExprFactory.minWithAlias("column2", "MIN2")),

                    PredicateFactory.isNotNull("column1")
                            .and(PredicateFactory.isNotNull("column2")),
                    Collections.singletonList(ColumnExprFactory.columnRef("column3"))));

            printResultSet(queryResult);
            checkHeaders(queryResult.getHeaders(), "column3", "SUM1", "MAX1", "MIN1",
                    "SUM2", "MAX2", "MIN2");
            assertEquals(3, queryResult.getRows().size());
            checkRowExists(queryResult, "test3", 12, 12, 12, 32, 32, 32);
            checkRowExists(queryResult, "test1", 25, 15, 10, 64, 34, 30);
            checkRowExists(queryResult, "test2", 24, 13, 11, 64, 33, 31);

            printResultSet(queryResult);
        } catch (SqlException se) {
            System.out.println(se.getMessage());
            fail();
        }
    }


}
