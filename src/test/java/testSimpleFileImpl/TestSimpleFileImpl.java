package testSimpleFileImpl;

import sqlFactory.SqlManagerFactory;
import clientDefaultImpl.SelectExpressionImpl;
import clientDefaultImpl.SqlClientImpl;
import api.ColumnReference;
import api.SqlServer;
import api.SqlClient;
import api.Table;
import api.exceptions.SqlException;
import clientDefaultImpl.SelectionPredicateImpl;
import api.selectionResult.ResultSet;

import java.util.Arrays;

public class TestSimpleFileImpl {
    public static void main(String[] args) {

        SqlServer sqlServer = SqlManagerFactory.getSimpleFileSqlManager();
        SqlClient qm = new SqlClientImpl();

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
                    qm.tableMetadata("table1", Arrays.asList(
                            qm.getIntegerColumnMetadataBuilder("column1").notNull().primaryKey().build(),
                            qm.getIntegerColumnMetadataBuilder("column2").build(),
                            qm.getVarcharColumnMetadataBuilder("column3", 20).notNull().build())));
        } catch (SqlException e) {
            System.out.println(e.getMessage());
        }


        try {
            sqlServer.getDatabase("DB1").createTable(
                    qm.tableMetadata("table1", Arrays.asList(
                            qm.getIntegerColumnMetadataBuilder("column11").notNull().primaryKey().build(),
                            qm.getIntegerColumnMetadataBuilder("column12").build(),
                            qm.getVarcharColumnMetadataBuilder("column13", 20).notNull().build()
                    )));
        } catch (SqlException e) {
            System.out.println(e.getMessage());
        }

        try {
            sqlServer.getDatabase("DB2").createTable(qm.tableMetadata("table2", Arrays.asList(
                    qm.getIntegerColumnMetadataBuilder("column11").notNull().primaryKey().build(),
                    qm.getIntegerColumnMetadataBuilder("column12").build(),
                    qm.getVarcharColumnMetadataBuilder("column13", 20).notNull().build())));
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
                    qm.tableMetadata("table2", Arrays.asList(
                            qm.getIntegerColumnMetadataBuilder("column4").notNull().primaryKey().build(),
                            qm.getVarcharColumnMetadataBuilder("column5", 15).build()
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
                    qm.getSelectionExpressionBuilder(qm.baseTableRef("table2", "DB1"))
                            .addTableReference(qm.baseTableRef("table1", "DB1"))
                            .addPredicateWithAnd(SelectionPredicateImpl.equals(new ColumnReference("column5", "table2"),
                                    new ColumnReference("column3", "table1")))
                            .addPredicateWithAnd(SelectionPredicateImpl.equals(new ColumnReference("column4", "table2"), 23))
//                   .addPredicateWithOr(SelectionPredicate.equals(new ColumnReference("column3", "table1"), "test2"))
                            .build());
            System.out.println(resultSet);

            ResultSet resultSet2 = sqlServer.select(SelectExpressionImpl.builder(
                    qm.innerJoin(qm.baseTableRef("table2", "DB1"),
                            qm.baseTableRef("table1", "DB1"),
                            SelectionPredicateImpl.equals(new ColumnReference("column5", "table2"),
                                    new ColumnReference("column3", "table1"))))
                    .addPredicateWithAnd(SelectionPredicateImpl.equals(new ColumnReference("column4", "table2"), 23))
                    .build());
            System.out.println(resultSet2);


        } catch (SqlException e) {
            System.out.println(e.getMessage());
        }


    }
}
