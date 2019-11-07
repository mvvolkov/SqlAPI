package testSimpleFileImpl;

import api.AssignmentOperation;
import api.SqlClient;
import api.SqlServer;
import api.exceptions.SqlException;
import clientDefaultImpl.ColumnReferenceImpl;
import clientDefaultImpl.SqlClientImpl;
import sqlFactory.SqlManagerFactory;

import java.util.Arrays;

public class TestSimplePrintOutImpl {

    public static void main(String[] args) {

        SqlServer sqlServer = SqlManagerFactory.getPrintOutSqlManager();
        SqlClient sqlClient = new SqlClientImpl();

        try {
            sqlServer.createDatabase("DB1");
        } catch (SqlException e) {
            System.out.println(e.getMessage());
        }

        try {
            sqlServer.createDatabase("DB1");
        } catch (SqlException e) {
            System.out.println(e.getMessage());
        }

        try {
            sqlServer.getDatabase("DB1").createTable(
                    sqlClient.tableMetadata("table1",
                            Arrays.asList(
                                    sqlClient.getIntegerColumnMetadataBuilder("column1").notNull().primaryKey().build(),
                                    sqlClient.getIntegerColumnMetadataBuilder("column2").build(),
                                    sqlClient.getVarcharColumnMetadataBuilder("column3", 20).notNull().build())));
        } catch (SqlException e) {
            System.out.println(e.getMessage());
        }

        try {
            sqlServer.getDatabase("DB1").getTable("table2").insert(Arrays.asList(10,
                    null, "test"));
        } catch (SqlException e) {
            System.out.println(e.getMessage());
        }

        try {
            sqlServer.getDatabase("DB1").getTable("table1").insert(Arrays.asList("column1", "column2", "column3"), Arrays.asList(10,
                    null, "test"));
        } catch (SqlException e) {
            System.out.println(e.getMessage());
        }

        try {
            sqlServer.getDatabase("DB1").getTable("table1").delete(sqlClient.getPredicateEmpty());
        } catch (SqlException e) {
            System.out.println(e.getMessage());
        }
        try {
            sqlServer.getDatabase("DB1").getTable("table1").
                    delete(
                            sqlClient.getPredicateEquals(new ColumnReferenceImpl("column1"), 3).or(
                                    sqlClient.getPredicateGreaterThan(new ColumnReferenceImpl("column2"), "12")).and(
                                    sqlClient.getPredicateIsNull(new ColumnReferenceImpl("column3")))
                    );
        } catch (SqlException e) {
            System.out.println(e.getMessage());
        }

        try {
            sqlServer.getDatabase("DB1").getTable("table1").
                    update(Arrays.asList(new AssignmentOperation("column1", 10),
                            new AssignmentOperation("column2", "test3")),
                            sqlClient.getPredicateLessThan(new ColumnReferenceImpl("column3"), "abs"));

            sqlServer.getDatabase("DB1").getTable("table1").
                    update(Arrays.asList(new AssignmentOperation("column1", 10),
                            new AssignmentOperation("column2", "test3")),
                            sqlClient.getPredicateIn(new ColumnReferenceImpl("column3"), Arrays.asList("12", "13", "14")));
        } catch (SqlException e) {
            System.out.println(e.getMessage());
        }

//        try {
//            ResultSet resultSet = sqlServer.getDatabase("DB1").getTable("table1").
//                    select(Arrays.asList(SelectedItemImpl.all()), sqlClient.getPredicateEmpty());
//        } catch (SqlException e) {
//            System.out.println(e.getMessage());
//        }
    }
}
