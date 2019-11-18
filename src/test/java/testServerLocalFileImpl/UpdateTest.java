package testServerLocalFileImpl;

import api.exceptions.SqlException;
import api.selectResult.ResultSet;
import clientImpl.columnExpr.ColumnExprFactory;
import clientImpl.misc.AssignmentOperationFactory;
import clientImpl.predicates.PredicateFactory;
import clientImpl.queries.SqlQueryFactory;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class UpdateTest extends AbstractServerLocalFileTest {

    @Test
    public void testUpdateOneColumnNoPredicate() {
        System.out.println("testUpdateOneColumnNoPredicate:");
        try {

            sqlServer.executeStatement(SqlQueryFactory.update("DB1", "table1",
                    Collections.singletonList(AssignmentOperationFactory.assign("column4", ColumnExprFactory.value("updtd")))));

            ResultSet resultSet = this.getTableData("DB1", "table1");
            assertEquals(4, resultSet.getColumns().size());
            assertEquals(6, resultSet.getRows().size());

            Map<String, Object> values = new HashMap<>();
            values.put("column1", 10);
            values.put("column2", 30);
            values.put("column3", "test1");
            values.put("column4", "updtd");
            checkRowExists(resultSet, values);

            values = new HashMap<>();
            values.put("column1", 11);
            values.put("column2", 31);
            values.put("column3", "test2");
            values.put("column4", "updtd");
            checkRowExists(resultSet, values);

            values = new HashMap<>();
            values.put("column1", 12);
            values.put("column2", 32);
            values.put("column3", "test3");
            values.put("column4", "updtd");
            checkRowExists(resultSet, values);

            values = new HashMap<>();
            values.put("column1", 13);
            values.put("column2", 33);
            values.put("column3", "test2");
            values.put("column4", "updtd");
            checkRowExists(resultSet, values);

            values = new HashMap<>();
            values.put("column1", 15);
            values.put("column2", 34);
            values.put("column3", "test1");
            values.put("column4", "updtd");
            checkRowExists(resultSet, values);

            values = new HashMap<>();
            values.put("column1", 16);
            values.put("column2", null);
            values.put("column3", "test1");
            values.put("column4", "updtd");
            checkRowExists(resultSet, values);

        } catch (SqlException se) {
            System.out.println(se.getMessage());
            fail();
        }
    }

    @Test
    public void testUpdateTwoColumnsNoPredicate() {
        System.out.println("testUpdateTwoColumnsNoPredicate:");
        try {

            sqlServer.executeStatement(SqlQueryFactory.update("DB1", "table1",
                    Arrays.asList(AssignmentOperationFactory.assign("column4", ColumnExprFactory.value("updtd")),
                            AssignmentOperationFactory.assign("column2", ColumnExprFactory.value(37)))));

            ResultSet resultSet = this.getTableData("DB1", "table1");
            assertEquals(4, resultSet.getColumns().size());
            assertEquals(6, resultSet.getRows().size());

            Map<String, Object> values = new HashMap<>();
            values.put("column1", 10);
            values.put("column2", 37);
            values.put("column3", "test1");
            values.put("column4", "updtd");
            checkRowExists(resultSet, values);

            values = new HashMap<>();
            values.put("column1", 11);
            values.put("column2", 37);
            values.put("column3", "test2");
            values.put("column4", "updtd");
            checkRowExists(resultSet, values);

            values = new HashMap<>();
            values.put("column1", 12);
            values.put("column2", 37);
            values.put("column3", "test3");
            values.put("column4", "updtd");
            checkRowExists(resultSet, values);

            values = new HashMap<>();
            values.put("column1", 13);
            values.put("column2", 37);
            values.put("column3", "test2");
            values.put("column4", "updtd");
            checkRowExists(resultSet, values);

            values = new HashMap<>();
            values.put("column1", 15);
            values.put("column2", 37);
            values.put("column3", "test1");
            values.put("column4", "updtd");
            checkRowExists(resultSet, values);

            values = new HashMap<>();
            values.put("column1", 16);
            values.put("column2", 37);
            values.put("column3", "test1");
            values.put("column4", "updtd");
            checkRowExists(resultSet, values);

        } catch (SqlException se) {
            System.out.println(se.getMessage());
            fail();
        }

    }

    @Test
    public void testUpdateTwoColumnsPredicateIsNull() {
        System.out.println("testUpdateTwoColumnsPredicateIsNull:");
        try {

            sqlServer.executeStatement(SqlQueryFactory.update("DB1", "table1",
                    Arrays.asList(AssignmentOperationFactory.assign("column4", ColumnExprFactory.value("updtd")),
                            AssignmentOperationFactory.assign("column2", ColumnExprFactory.value(37))),
                    PredicateFactory.isNull(ColumnExprFactory.columnRef("column4"))));

            ResultSet resultSet = this.getTableData("DB1", "table1");
            assertEquals(4, resultSet.getColumns().size());
            assertEquals(6, resultSet.getRows().size());

            Map<String, Object> values = new HashMap<>();
            values.put("column1", 10);
            values.put("column2", 30);
            values.put("column3", "test1");
            values.put("column4", "t21");
            checkRowExists(resultSet, values);

            values = new HashMap<>();
            values.put("column1", 11);
            values.put("column2", 37);
            values.put("column3", "test2");
            values.put("column4", "updtd");
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
            values.put("column2", 37);
            values.put("column3", "test1");
            values.put("column4", "updtd");
            checkRowExists(resultSet, values);

            values = new HashMap<>();
            values.put("column1", 16);
            values.put("column2", 37);
            values.put("column3", "test1");
            values.put("column4", "updtd");
            checkRowExists(resultSet, values);

        } catch (SqlException se) {
            System.out.println(se.getMessage());
            fail();
        }

    }


}
