package testServerLocalFileImpl;

import sqlapi.exceptions.SqlException;
import sqlapi.selectResult.ResultSet;
import clientImpl.columnExpr.ColumnExprFactory;
import clientImpl.misc.AssignmentOperationFactory;
import clientImpl.predicates.PredicateFactory;
import clientImpl.queries.SqlQueryFactory;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class UpdateTest extends AbstractServerLocalFileTest {

    @Test
    public void testUpdateOneColumnNoPredicate() {
        System.out.println("testUpdateOneColumnNoPredicate:");
        try {

            sqlServer.executeStatement(SqlQueryFactory.update("DB1", "table1",
                    Collections.singletonList(AssignmentOperationFactory
                            .assign("column4", ColumnExprFactory.value("updtd")))));

            ResultSet resultSet = this.getTableData("DB1", "table1");
            assertEquals(4, resultSet.getHeaders().size());
            assertEquals(6, resultSet.getRows().size());
            checkRowExists(resultSet, 10, 30, "test1", "updtd");
            checkRowExists(resultSet, 11, 31, "test2", "updtd");
            checkRowExists(resultSet, 12, 32, "test3", "updtd");
            checkRowExists(resultSet, 13, 33, "test2", "updtd");
            checkRowExists(resultSet, 15, 34, "test1", "updtd");
            checkRowExists(resultSet, 16, null, "test1", "updtd");

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
                    Arrays.asList(AssignmentOperationFactory
                                    .assign("column4", ColumnExprFactory.value("updtd")),
                            AssignmentOperationFactory
                                    .assign("column2", ColumnExprFactory.value(37)))));

            ResultSet resultSet = this.getTableData("DB1", "table1");
            assertEquals(4, resultSet.getHeaders().size());
            assertEquals(6, resultSet.getRows().size());
            checkRowExists(resultSet, 10, 37, "test1", "updtd");
            checkRowExists(resultSet, 11, 37, "test2", "updtd");
            checkRowExists(resultSet, 12, 37, "test3", "updtd");
            checkRowExists(resultSet, 13, 37, "test2", "updtd");
            checkRowExists(resultSet, 15, 37, "test1", "updtd");
            checkRowExists(resultSet, 16, 37, "test1", "updtd");

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
                    Arrays.asList(AssignmentOperationFactory
                                    .assign("column4", ColumnExprFactory.value("updtd")),
                            AssignmentOperationFactory
                                    .assign("column2", ColumnExprFactory.value(37))),
                    PredicateFactory.isNull(ColumnExprFactory.columnRef("column4"))));

            ResultSet resultSet = this.getTableData("DB1", "table1");
            assertEquals(4, resultSet.getHeaders().size());
            assertEquals(6, resultSet.getRows().size());
            checkRowExists(resultSet, 10, 30, "test1", "t21");
            checkRowExists(resultSet, 11, 37, "test2", "updtd");
            checkRowExists(resultSet, 12, 32, "test3", "t43");
            checkRowExists(resultSet, 13, 33, "test2", "t653");
            checkRowExists(resultSet, 15, 37, "test1", "updtd");
            checkRowExists(resultSet, 16, 37, "test1", "updtd");

        } catch (SqlException se) {
            System.out.println(se.getMessage());
            fail();
        }

    }


}
