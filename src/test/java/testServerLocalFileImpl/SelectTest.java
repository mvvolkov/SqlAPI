package testServerLocalFileImpl;

import api.columnExpr.ColumnRef;
import api.exceptions.SqlException;
import api.misc.SelectedItem;
import api.selectResult.ResultSet;
import clientImpl.columnExpr.ColumnExprFactory;
import clientImpl.predicates.PredicateFactory;
import clientImpl.queries.SqlQueryFactory;
import clientImpl.tableRef.TableRefFactory;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class SelectTest extends AbstractServerLocalFileTest {

    @Test
    public void testSelectAll() {
        System.out.println("testSelectAll:");
        try {
            // SELECT * FROM DB1.table1;
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

    @Test
    public void testSelectColumns() {
        System.out.println("testSelectAll:");
        try {
            // SELECT column2, column3, column1 FROM DB1.table1;
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

    @Test
    public void testSelectColumnsAndValues() {
        System.out.println("testSelectAll:");
        try {
            // SELECT 15, column2, column3, column1, null FROM DB1.table1;
            ResultSet resultSet = sqlServer.getQueryResult(SqlQueryFactory.select(
                    TableRefFactory.dbTable("DB1", "table1"),
                    Arrays.asList(ColumnExprFactory.value(15),
                            ColumnExprFactory.columnRef("column2"),
                            ColumnExprFactory.columnRef("column3"),
                            ColumnExprFactory.columnRef("column1"),
                            ColumnExprFactory.value(null)
                    )));

            checkHeaders(resultSet.getHeaders(), "15", "column2", "column3", "column1",
                    "null");
            assertEquals(6, resultSet.getRows().size());
            checkRowExists(resultSet, 15, 30, "test1", 10, null);
            checkRowExists(resultSet, 15, 31, "test2", 11, null);
            checkRowExists(resultSet, 15, 32, "test3", 12, null);
            checkRowExists(resultSet, 15, 33, "test2", 13, null);
            checkRowExists(resultSet, 15, 34, "test1", 15, null);
            checkRowExists(resultSet, 15, null, "test1", 16, null);

        } catch (SqlException e) {
            System.out.println(e.getMessage());
            fail();
        }
    }

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


    @Test
    public void testSelectFromTwoTables() {

        System.out.println("testSelectFromTwoTables:");

        try {
            //SELECT ((column1 + column5) + 35) AS sum1, column2, column1, table1.column3,
            // column4, column5, table2.column3 FROM table2, table1 WHERE
            // (table2.column3 = table1.column3) AND (column4 IS NOT NULL);
            ResultSet resultSet =
                    sqlServer.getQueryResult(SqlQueryFactory.select(Arrays.asList(
                            TableRefFactory.dbTable("DB1", "table2"),
                            TableRefFactory.dbTable("DB1", "table1")),
                            Arrays.asList(
                                    ColumnExprFactory.sum(ColumnExprFactory.sum(
                                            ColumnExprFactory.columnRef("column1"),
                                            ColumnExprFactory.columnRef("column5")),
                                            ColumnExprFactory.value(35), "sum1"),
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

            checkHeaders(resultSet.getHeaders(), "sum1", "column2", "column1", "column3",
                    "column4", "column5", "column3");
            assertEquals(2, resultSet.getRows().size());
            checkRowExists(resultSet, 69, 32, 12, "test3", "t43", 22, "test3");
            checkRowExists(resultSet, 71, 33, 13, "test2", "t653", 23, "test2");


        } catch (SqlException se) {
            System.out.println(se.getMessage());
        }
    }
}
