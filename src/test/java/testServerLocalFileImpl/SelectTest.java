package testServerLocalFileImpl;

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
            ResultSet resultSet = sqlServer.getQueryResult(SqlQueryFactory.select(TableRefFactory.dbTable("DB1", "table1")));
            List<String> headers = resultSet.getColumns();

            assertEquals(4, headers.size());
            assertEquals(headers.get(0), "column1");
            assertEquals(headers.get(1), "column2");
            assertEquals(headers.get(2), "column3");
            assertEquals(headers.get(3), "column4");

            assertEquals(6, resultSet.getRows().size());

            Map<String, Object> values = new HashMap<>();
            values.put("column1", 10);
            values.put("column2", 30);
            values.put("column3", "test1");
            values.put("column4", "t21");
            checkRowExists(resultSet, values);

            values = new HashMap<>();
            values.put("column1", 11);
            values.put("column2", 31);
            values.put("column3", "test2");
            values.put("column4", null);
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

            List<String> headers = resultSet.getColumns();

            assertEquals(3, headers.size());
            assertEquals(headers.get(0), "column2");
            assertEquals(headers.get(1), "column3");
            assertEquals(headers.get(2), "column1");

            assertEquals(6, resultSet.getRows().size());

            Map<String, Object> values = new HashMap<>();
            values.put("column1", 10);
            values.put("column2", 30);
            values.put("column3", "test1");
            checkRowExists(resultSet, values);

            values = new HashMap<>();
            values.put("column1", 11);
            values.put("column2", 31);
            values.put("column3", "test2");
            checkRowExists(resultSet, values);

            values = new HashMap<>();
            values.put("column1", 12);
            values.put("column2", 32);
            values.put("column3", "test3");
            checkRowExists(resultSet, values);

            values = new HashMap<>();
            values.put("column1", 13);
            values.put("column2", 33);
            values.put("column3", "test2");
            checkRowExists(resultSet, values);

            values = new HashMap<>();
            values.put("column1", 15);
            values.put("column2", 34);
            values.put("column3", "test1");
            checkRowExists(resultSet, values);

            values = new HashMap<>();
            values.put("column1", 16);
            values.put("column2", null);
            values.put("column3", "test1");
            checkRowExists(resultSet, values);
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
                    PredicateFactory.equals(ColumnExprFactory.columnRef("column5"), ColumnExprFactory.value(22))
                            .and(PredicateFactory.equals(ColumnExprFactory.columnRef("column1"),
                                    ColumnExprFactory.value(13)))
            ));

            List<String> headers = resultSet.getColumns();

            assertEquals(5, headers.size());
            assertEquals(headers.get(0), "column5");
            assertEquals(headers.get(1), "column3");
            assertEquals(headers.get(2), "column2");
            assertEquals(headers.get(3), "column3");
            assertEquals(headers.get(4), "column1");

            assertEquals(1, resultSet.getRows().size());

            Map<String, Object> values = new HashMap<>();
            values.put("column1", 10);
            values.put("column2", 30);
            values.put("column3", "test1");
            checkRowExists(resultSet, values);


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
            ResultSet resultSet = sqlServer.getQueryResult(SqlQueryFactory.select(Arrays.asList(
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

            assertEquals(7, resultSet.getColumns().size());
            assertEquals(2, resultSet.getRows().size());

            Map<String, Object> values = new HashMap<>();
            values.put("sum1", 69);
            values.put("column1", 12);
            values.put("column2", 32);
            values.put("column3", "test3");
            values.put("column4", "t43");
            checkRowExists(resultSet, values);

            values = new HashMap<>();
            values.put("sum1", 71);
            values.put("column1", 13);
            values.put("column2", 33);
            values.put("column3", "test2");
            values.put("column4", "t653");
            checkRowExists(resultSet, values);

        } catch (SqlException se) {
            System.out.println(se.getMessage());
        }
    }
}
