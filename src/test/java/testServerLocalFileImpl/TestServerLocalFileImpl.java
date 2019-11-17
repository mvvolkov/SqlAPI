package testServerLocalFileImpl;

import api.connect.SqlServer;
import api.exceptions.ConstraintException;
import api.exceptions.NoSuchColumnException;
import api.exceptions.SqlException;
import api.metadata.ColumnMetadata;
import api.selectResult.ResultRow;
import api.selectResult.ResultSet;
import clientImpl.columnExpr.ColumnExprFactory;
import clientImpl.metadata.MetadataFactory;
import clientImpl.predicates.PredicateFactory;
import clientImpl.queries.SqlQueryFactory;
import clientImpl.tableRef.TableRefFactory;
import org.junit.Before;
import org.junit.Test;

import sqlFactory.SqlManagerFactory;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class TestServerLocalFileImpl {

    private SqlServer sqlServer;

    @Before
    public void setUp() {
        sqlServer = SqlManagerFactory.getServerLocalFileSqlManager();
        try {
            // Create a database
            sqlServer.createDatabase("DB1");

            // Create a table
            sqlServer.executeStatement(SqlQueryFactory
                    .createTable("DB1", "table1", Arrays.<ColumnMetadata<?>>asList(
                            MetadataFactory.integerBuilder("column1").notNull()
                                    .primaryKey().build(),
                            MetadataFactory.integerBuilder("column2").defaultValue(15)
                                    .build(),
                            MetadataFactory.varcharBuilder("column3", 20)
                                    .notNull()
                                    .build()
                    )));

            // Fill table1
            sqlServer.executeStatement(SqlQueryFactory.insert("DB1", "table1",
                    Arrays.asList(10, 30, "test1")));
            sqlServer.executeStatement(SqlQueryFactory.insert("DB1", "table1",
                    Arrays.asList(11, 31, "test2")));
            sqlServer.executeStatement(SqlQueryFactory.insert("DB1", "table1",
                    Arrays.asList(12, 32, "test3")));
            sqlServer.executeStatement(SqlQueryFactory.insert("DB1", "table1",
                    Arrays.asList(13, 33, "test2")));
            sqlServer.executeStatement(SqlQueryFactory.insert("DB1", "table1",
                    Arrays.asList(15, 34, "test1")));
            sqlServer.executeStatement(SqlQueryFactory.insert("DB1", "table1",
                    Arrays.asList(16, null, "test1")));

            // Create a table
            sqlServer.executeStatement(
                    SqlQueryFactory.createTable("DB1", "table2",
                            Arrays.<ColumnMetadata<?>>asList(
                                    MetadataFactory.integerBuilder("column4")
                                            .notNull().primaryKey().build(),
                                    MetadataFactory.varcharBuilder("column3", 15)
                                            .build()
                            )));

            // Fill table2
            sqlServer.executeStatement(
                    SqlQueryFactory.insert("DB1", "table2", Arrays.asList(22, "test3")));
            sqlServer.executeStatement(
                    SqlQueryFactory.insert("DB1", "table2", Arrays.asList(23, "test2")));
            sqlServer.executeStatement(
                    SqlQueryFactory.insert("DB1", "table2", Arrays.asList(25, "test4")));

        } catch (SqlException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testInsert1() {
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
    public void testInsert2() {
        try {
            sqlServer.executeStatement(SqlQueryFactory.insert("DB1", "table1",
                    Arrays.asList(10, 42, "test")));
        } catch (ConstraintException ce) {
            assertEquals("Constraint violation for the column dbo.table1.column1: " +
                    "PRIMARY KEY", ce.getMessage());
        } catch (SqlException se) {
            fail();
        }
    }

    @Test
    public void testInsert3() {
        try {
            sqlServer.executeStatement(SqlQueryFactory.insert("DB1", "table1",
                    Arrays.asList(21, 43, null)));
        } catch (ConstraintException ce) {
            assertEquals(
                    "Constraint violation for the column dbo.table1.column3: NOT NULL",
                    ce.getMessage());
        } catch (SqlException se) {
            fail();
        }
    }

    private static void checkRow(ResultSet
                                         resultSet, Map<String, Object> keyValues,
                                 Map<String, Object> otherValues) {
        ResultRow resultRow = null;
        try {
            for (ResultRow row : resultSet.getRows()) {
                boolean rowMatch = true;
                for (Map.Entry<String, Object> entry : keyValues.entrySet()) {
                    if (!row.getObject(entry.getKey()).equals(entry.getValue())) {
                        rowMatch = false;
                        break;
                    }
                }
                if (rowMatch) {
                    resultRow = row;
                    break;
                }
            }
            assertNotNull(resultRow);

            for (Map.Entry<String, Object> entry : otherValues.entrySet()) {
                assertEquals(entry.getValue(), resultRow.getObject(entry.getKey()));
            }
        } catch (SqlException e) {
            System.out.println(e.getMessage());
            fail();
        }
    }

    public static void main(String[] args) {

        System.out.println("");
        SqlServer sqlServer = SqlManagerFactory.getServerLocalFileSqlManager();


        try {

            sqlServer.createDatabase("DB1");

            sqlServer.executeStatement(SqlQueryFactory
                    .createTable("DB1", "table1", Arrays.<ColumnMetadata<?>>asList(
                            MetadataFactory.integerBuilder("column1").notNull()
                                    .primaryKey().build(),
                            MetadataFactory.integerBuilder("column2").defaultValue(15)
                                    .build(),
                            MetadataFactory.varcharBuilder("column3", 20)
                                    .notNull()
                                    .build()
                    )));

            sqlServer.executeStatement(SqlQueryFactory.insert("DB1", "table1",
                    Arrays.asList(10, 30, "test1")
            ));
            sqlServer.executeStatement(SqlQueryFactory.insert("DB1", "table1",
                    Arrays.asList(11, 31, "test2")));
            sqlServer.executeStatement(SqlQueryFactory.insert("DB1", "table1",
                    Arrays.asList(12, 32, "test3")));
            sqlServer.executeStatement(SqlQueryFactory.insert("DB1", "table1",
                    Arrays.asList(13, 33, "test2")));
            sqlServer.executeStatement(SqlQueryFactory.insert("DB1", "table1",
                    Arrays.asList(15, 34, "test1")));


            System.out.println("");
            printResultSet(sqlServer.getQueryResult(
                    SqlQueryFactory
                            .select(TableRefFactory.dbTable("DB1", "table1"))));

            sqlServer.executeStatement(
                    SqlQueryFactory.createTable("DB1", "table2",
                            Arrays.<ColumnMetadata<?>>asList(
                                    MetadataFactory.integerBuilder("column4")
                                            .notNull().primaryKey().build(),
                                    MetadataFactory.varcharBuilder("column3", 15)
                                            .build()
                            )));

            sqlServer.executeStatement(
                    SqlQueryFactory
                            .insert("DB1", "table2", Arrays.asList(22, "test3")));
            sqlServer.executeStatement(
                    SqlQueryFactory
                            .insert("DB1", "table2", Arrays.asList(23, "test2")));
            sqlServer.executeStatement(
                    SqlQueryFactory
                            .insert("DB1", "table2", Arrays.asList(25, "test4")));
            System.out.println("");
            printResultSet(sqlServer.getQueryResult(
                    SqlQueryFactory
                            .select(TableRefFactory.dbTable("DB1", "table2"))));

            System.out.println("");
            printResultSet(
                    sqlServer.getQueryResult(SqlQueryFactory.select(Arrays.asList(
                            TableRefFactory.dbTable("DB1", "table2"),
                            TableRefFactory.dbTable("DB1", "table1")),
                            Arrays.asList(
                                    ColumnExprFactory.sum(ColumnExprFactory.sum(
                                            ColumnExprFactory.columnRef("column2"),
                                            ColumnExprFactory.columnRef("column1")),
                                            ColumnExprFactory.value(35),
                                            "column1 + column2 + 35"),
                                    ColumnExprFactory.columnRef("column2"),
                                    ColumnExprFactory.columnRef("column1"),
                                    ColumnExprFactory.columnRef("table1", "column3"),
                                    ColumnExprFactory.columnRef("column4"),
                                    ColumnExprFactory.columnRef("table2", "column3")
                            ),
                            PredicateFactory
                                    .equals(ColumnExprFactory
                                                    .columnRef("table2", "column3"),
                                            ColumnExprFactory
                                                    .columnRef("table1", "column3"))
                                    .and(PredicateFactory
                                            .equals(ColumnExprFactory
                                                            .columnRef("column4"),
                                                    ColumnExprFactory
                                                            .integer(23))))));

            System.out.println("");
            printResultSet(
                    sqlServer.getQueryResult(SqlQueryFactory.select(TableRefFactory
                            .innerJoin(TableRefFactory.dbTable("DB1", "table2"),
                                    TableRefFactory.dbTable("DB1", "table1"),
                                    PredicateFactory
                                            .equals(ColumnExprFactory
                                                            .columnRef("table2", "column3"),
                                                    ColumnExprFactory
                                                            .columnRef("table1",
                                                                    "column3"))
                                            .and(PredicateFactory
                                                    .equals(ColumnExprFactory
                                                                    .columnRef("column2"),
                                                            ColumnExprFactory
                                                                    .sum(ColumnExprFactory
                                                                                    .columnRef(
                                                                                            "column4"),
                                                                            ColumnExprFactory
                                                                                    .integer(
                                                                                            10))))))));

            System.out.println("");
            printResultSet(
                    sqlServer.getQueryResult(
                            SqlQueryFactory
                                    .select(TableRefFactory.dbTable("DB1", "table1"),
                                            PredicateFactory.in(ColumnExprFactory
                                                            .columnRef("column1"),
                                                    Arrays.asList(
                                                            ColumnExprFactory
                                                                    .integer(15),
                                                            ColumnExprFactory
                                                                    .integer(12),
                                                            ColumnExprFactory
                                                                    .integer(13)
                                                    )
                                            ))));


            sqlServer.executeStatement(
                    SqlQueryFactory
                            .insert("DB1", "table1",
                                    Arrays.asList(101, null, "test101")
                            ));
            System.out.println("");
            printResultSet(sqlServer.getQueryResult(
                    SqlQueryFactory
                            .select(TableRefFactory.dbTable("DB1", "table1"))));

            System.out.println("");
            printResultSet(sqlServer.getQueryResult(SqlQueryFactory
                    .select(TableRefFactory.dbTable("DB1", "table1"), PredicateFactory
                            .isNull(ColumnExprFactory.columnRef("column2")))));

            System.out.println("");
            printResultSet(
                    sqlServer.getQueryResult(
                            SqlQueryFactory.select(TableRefFactory.dbTable("DB1",
                                    "table1"),
                                    PredicateFactory
                                            .isNotNull(
                                                    ColumnExprFactory
                                                            .columnRef("column2"
                                                            )).and(PredicateFactory
                                            .equals(ColumnExprFactory.columnRef(
                                                    "column2"),
                                                    ColumnExprFactory
                                                            .integer(30))))));

            sqlServer.executeStatement(SqlQueryFactory.insert("DB1", "table2",
                    SqlQueryFactory.select(TableRefFactory.dbTable("DB1", "table1"),
                            Arrays.asList(ColumnExprFactory.columnRef("column2"),
                                    ColumnExprFactory.columnRef("column3")),
                            PredicateFactory
                                    .isNotNull(ColumnExprFactory.columnRef("column2"
                                    )))));
            System.out.println("");
            printResultSet(sqlServer.getQueryResult(
                    SqlQueryFactory
                            .select(TableRefFactory.dbTable("DB1", "table2"))));

            System.out.println("");
            printResultSet(sqlServer.getQueryResult(SqlQueryFactory.select(
                    Arrays.asList(
                            TableRefFactory.dbTable("DB1", "table2"),
                            TableRefFactory.tableFromSelect(
                                    SqlQueryFactory
                                            .select(TableRefFactory
                                                            .dbTable("DB1", "table1"),
                                                    Arrays.asList(
                                                            ColumnExprFactory
                                                                    .columnRef(
                                                                            "column2"),
                                                            ColumnExprFactory
                                                                    .columnRef(
                                                                            "column3")
                                                    )), "t1")
                    ),
                    PredicateFactory
                            .equals(ColumnExprFactory.columnRef("table2", "column3"),
                                    ColumnExprFactory.columnRef("t1", "column3"))
                    ))
            );

            printResultSet(sqlServer.getQueryResult(SqlQueryFactory.selectGrouped(
                    TableRefFactory.dbTable("DB1", "table2"),
                    Arrays.asList(ColumnExprFactory.columnRef("column3"),
                            ColumnExprFactory.sum(ColumnExprFactory.countAll(),
                                    ColumnExprFactory.integer(1), "C1")),
                    Collections
                            .singletonList(ColumnExprFactory.columnRef("column3")))));

            printResultSet(sqlServer.getQueryResult(SqlQueryFactory.selectGrouped(
                    TableRefFactory.dbTable("DB1", "table2"),
                    Arrays.asList(ColumnExprFactory.columnRef("column3"),
                            ColumnExprFactory.aggregateSum("column4")),
                    Collections
                            .singletonList(ColumnExprFactory.columnRef("column3")))));

            printResultSet(sqlServer.getQueryResult(SqlQueryFactory.selectGrouped(
                    TableRefFactory.dbTable("DB1", "table2"),
                    Arrays.asList(ColumnExprFactory.columnRef("column3"),
                            ColumnExprFactory.max("column4")),
                    Collections
                            .singletonList(ColumnExprFactory.columnRef("column3")))));

            printResultSet(sqlServer.getQueryResult(SqlQueryFactory.selectGrouped(
                    TableRefFactory.dbTable("DB1", "table2"),
                    Arrays.asList(ColumnExprFactory.columnRef("column3"),
                            ColumnExprFactory.min("column4")),
                    Collections
                            .singletonList(ColumnExprFactory.columnRef("column3")))));


        } catch (SqlException e) {
            System.out.println(e.getMessage());
        }
    }


    private static void printResultSet(ResultSet resultSet)
            throws NoSuchColumnException {

        StringBuilder sb = new StringBuilder();

        String columnsHeaders = resultSet.getColumns().stream()
                .collect(Collectors.joining(", "));

        sb.append(columnsHeaders);
        for (ResultRow row : resultSet.getRows()) {
            List<String> values = new ArrayList<>();
            for (String columnName : resultSet.getColumns()) {
                values.add(String.valueOf(row.getObject(columnName)));
            }
            String rowString =
                    values.stream().collect(Collectors.joining(", "));
            sb.append("\n" + rowString);
        }
        System.out.println(sb);
        System.out.println("");
    }
}
