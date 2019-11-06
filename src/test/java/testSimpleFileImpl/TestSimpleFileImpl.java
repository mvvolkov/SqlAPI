package testSimpleFileImpl;

import api.ColumnReference;
import api.SqlClient;
import api.SqlServer;
import api.Table;
import api.exceptions.SqlException;
import api.selectionResult.ResultSet;
import clientDefaultImpl.SelectExpressionImpl;
import clientDefaultImpl.SqlClientImpl;
import sqlFactory.SqlManagerFactory;

import java.util.Arrays;

public class TestSimpleFileImpl {
    public static void main(String[] args) {

        SqlServer sqlServer = SqlManagerFactory.getSimpleFileSqlManager();
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
                    sqlClient.tableMetadata("table1", Arrays.asList(
                            sqlClient.getIntegerColumnMetadataBuilder("column1").notNull().primaryKey().build(),
                            sqlClient.getIntegerColumnMetadataBuilder("column2").build(),
                            sqlClient.getVarcharColumnMetadataBuilder("column3", 20).notNull().build())));
        } catch (SqlException e) {
            System.out.println(e.getMessage());
        }


        try {
            sqlServer.getDatabase("DB1").createTable(
                    sqlClient.tableMetadata("table1", Arrays.asList(
                            sqlClient.getIntegerColumnMetadataBuilder("column11").notNull().primaryKey().build(),
                            sqlClient.getIntegerColumnMetadataBuilder("column12").build(),
                            sqlClient.getVarcharColumnMetadataBuilder("column13", 20).notNull().build()
                    )));
        } catch (SqlException e) {
            System.out.println(e.getMessage());
        }

        try {
            sqlServer.getDatabase("DB2").createTable(sqlClient.tableMetadata("table2", Arrays.asList(
                    sqlClient.getIntegerColumnMetadataBuilder("column11").notNull().primaryKey().build(),
                    sqlClient.getIntegerColumnMetadataBuilder("column12").build(),
                    sqlClient.getVarcharColumnMetadataBuilder("column13", 20).notNull().build())));
        } catch (SqlException e) {
            System.out.println(e.getMessage());
        }

        try {
            Table table1 = sqlServer.getDatabase("DB1").getTable("table1");
            table1.insert(Arrays.asList(10, 20, null));
        } catch (SqlException e) {
            System.out.println(e.getMessage());
        }

        try {
            Table table1 = sqlServer.getDatabase("DB1").getTable("table1");
            table1.insert(Arrays.asList(10, 20, "123456789012345678901234567890"));
        } catch (SqlException e) {
            System.out.println(e.getMessage());
        }

        try {
            Table table1 = sqlServer.getDatabase("DB1").getTable("table1");
            table1.insert(Arrays.asList(10, 20, "test1"));
            table1.insert(Arrays.asList(11, 20, "test1"));
            table1.insert(Arrays.asList(10, 20, "test1"));
        } catch (SqlException e) {
            System.out.println(e.getMessage());
        }

        try {
            Table table1 = sqlServer.getDatabase("DB1").getTable("table1");
            table1.insert(Arrays.asList(12, 20, "test1"));
            table1.insert(Arrays.asList(13, 20, "test2"));
            table1.insert(Arrays.asList(15, 21, "test1"));
        } catch (SqlException e) {
            System.out.println(e.getMessage());
        }

//        try {
//            ResultSet resultSet = sqlManager.select(SelectExpression.builder(
//                    BaseTableReference.newTableReference("table1", "DB1"))
//                    .addPredicateWithAnd(SelectionPredicate.equals(
//                            new ColumnReference("column3"), 10)).build());
//            System.out.println(resultSet);
//        } catch (SqlException e) {
//            System.out.println(e.getMessage());
//        }
//
//        try {
//            ResultSet resultSet = sqlManager.select(SelectExpression.builder(
//                    BaseTableReference.newTableReference("table1", "DB1"))
//                    .build());
//            System.out.println(resultSet);
//        } catch (SqlException e) {
//            System.out.println(e.getMessage());
//        }
//
//        try {
//            ResultSet resultSet = sqlManager.select(SelectExpression.builder(
//                    BaseTableReference.newTableReference("table1", "DB1"))
//                    .addPredicateWithAnd(SelectionPredicate.equals(
//                            new ColumnReference("column3"), "test1")).build());
//            System.out.println(resultSet);
//        } catch (SqlException e) {
//            System.out.println(e.getMessage());
//        }

        try {
            sqlServer.getDatabase("DB1").createTable(
                    sqlClient.tableMetadata("table2", Arrays.asList(
                            sqlClient.getIntegerColumnMetadataBuilder("column4").notNull().primaryKey().build(),
                            sqlClient.getVarcharColumnMetadataBuilder("column5", 15).build()
                    )));
            Table table2 = sqlServer.getDatabase("DB1").getTable("table2");
            table2.insert(Arrays.asList(22, "test2"));
            table2.insert(Arrays.asList(23, "test2"));
            table2.insert(Arrays.asList(25, "test22"));

//            ResultSet resultSet = sqlManager.select(SelectExpression.builder(
//                    BaseTableReference.newTableReference("table2", "DB1"))
//                    .build());
//            System.out.println(resultSet);

            ResultSet resultSet = sqlServer.select(
                    sqlClient.getSelectionExpressionBuilder(sqlClient.baseTableRef("table2", "DB1"))
                            .addTableReference(sqlClient.baseTableRef("table1", "DB1"))
                            .addPredicateWithAnd(sqlClient.getPredicateEquals(new ColumnReference("column5", "table2"),
                                    new ColumnReference("column3", "table1")))
                            .addPredicateWithAnd(sqlClient.getPredicateEquals(new ColumnReference("column4", "table2"), 23))
//                   .addPredicateWithOr(SelectionPredicate.equals(new ColumnReference("column3", "table1"), "test2"))
                            .build());
            System.out.println(resultSet);

            ResultSet resultSet2 = sqlServer.select(SelectExpressionImpl.builder(
                    sqlClient.innerJoin(sqlClient.baseTableRef("table2", "DB1"),
                            sqlClient.baseTableRef("table1", "DB1"),
                            sqlClient.getPredicateEquals(new ColumnReference("column5", "table2"),
                                    new ColumnReference("column3", "table1"))))
                    .addPredicateWithAnd(sqlClient.getPredicateEquals(new ColumnReference("column4", "table2"), 23))
                    .build());
            System.out.println(resultSet2);


        } catch (SqlException e) {
            System.out.println(e.getMessage());
        }


    }
}
