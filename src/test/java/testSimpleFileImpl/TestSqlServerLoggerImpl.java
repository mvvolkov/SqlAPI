package testSimpleFileImpl;

import clientDefaultImpl.AssignmentOperationImpl;
import api.SqlClient;
import api.SqlServer;
import api.exceptions.SqlException;
import clientDefaultImpl.ColumnReferenceImpl;
import clientDefaultImpl.SqlClientImpl;
import sqlFactory.SqlManagerFactory;

import java.util.Arrays;

public class TestSqlServerLoggerImpl {

    public static void main(String[] args) {

        SqlServer sqlServer = SqlManagerFactory.getPrintOutSqlManager();
        SqlClient sqlClient = new SqlClientImpl();

        try {
            sqlServer.createDatabase("DB1");

            sqlServer.executeStatement(sqlClient.newCreateTableStatement("DB1", "table1", Arrays.asList(
                    sqlClient.getIntegerColumnMetadataBuilder("column1").notNull().primaryKey().build(),
                    sqlClient.getIntegerColumnMetadataBuilder("column2").build(),
                    sqlClient.getVarcharColumnMetadataBuilder("column3", 20).notNull().build())));

            sqlServer.executeStatement(sqlClient.newInsertStatement("DB1", "table2", Arrays.asList(10,
                    null, "test")));

            sqlServer.executeStatement(sqlClient.newInsertStatement("DB1", "table1",
                    Arrays.asList("column1", "column2", "column3"), Arrays.asList(10,
                            null, "test")));

            sqlServer.executeStatement(sqlClient.newDeleteStatement("DB1", "table1"));

            sqlServer.executeStatement(sqlClient.newDeleteStatement("DB1", "table1",
                    sqlClient.getPredicateEquals(new ColumnReferenceImpl("column1"), 3).or(
                            sqlClient.getPredicateGreaterThan(new ColumnReferenceImpl("column2"), "12")).and(
                            sqlClient.getPredicateIsNull(new ColumnReferenceImpl("column3")))));


            sqlServer.executeStatement(sqlClient.newUpdateStatement("DB1", "table1",
                    Arrays.asList(new AssignmentOperationImpl("column1", 10),
                            new AssignmentOperationImpl("column2", "test3")),
                    sqlClient.getPredicateLessThan(new ColumnReferenceImpl("column3"), "abs")));


            sqlServer.executeStatement(sqlClient.newUpdateStatement("DB1", "table1",
                    Arrays.asList(new AssignmentOperationImpl("column1", 10),
                            new AssignmentOperationImpl("column2", "test3")),
                    sqlClient.getPredicateIn(new ColumnReferenceImpl("column3"), Arrays.asList("12", "13", "14"))
            ));


        } catch (SqlException e) {
            System.out.println(e.getMessage());
        }


    }
}
