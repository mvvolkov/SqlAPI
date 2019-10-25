import SqlManagerFactory.SqlManagerFactory;
import sqlapi.*;
import sqlapi.dbMetadata.IntegerColumnMetadata;
import sqlapi.dbMetadata.TableMetadata;
import sqlapi.dbMetadata.VarcharColumnMetadata;
import sqlapi.exceptions.*;
import sqlapi.selectionPredicate.SelectionPredicate;
import sqlapi.selectionResult.ResultSet;

import java.util.Arrays;

public class TestSimpleFileImpl {
    public static void main(String[] args) {

        SqlManager sqlManager = SqlManagerFactory.getSimpleFileSqlManager();

        try {
            sqlManager.createDatabase("DB1");
        } catch (SqlException e) {
            System.out.println(e.getMessage());
        }

        try {
            sqlManager.createDatabase("DB1");
        } catch (SqlException e) {
            System.out.println(e.getMessage());
        }

        try {
            sqlManager.getDatabase("DB1").createTable(new TableMetadata("table1",
                    Arrays.asList(IntegerColumnMetadata.builder("column1").notNull().primaryKey().build(),
                            IntegerColumnMetadata.builder("column2").build(),
                            VarcharColumnMetadata.builder("column3", 20).notNull().build())));
        } catch (SqlException e) {
            System.out.println(e.getMessage());
        }

        try {
            sqlManager.getDatabase("DB1").createTable(new TableMetadata("table1",
                    Arrays.asList(IntegerColumnMetadata.builder("column11").notNull().primaryKey().build(),
                            IntegerColumnMetadata.builder("column12").build(),
                            VarcharColumnMetadata.builder("column13", 20).notNull().build())));
        } catch (SqlException e) {
            System.out.println(e.getMessage());
        }

        try {
            sqlManager.getDatabase("DB2").createTable(new TableMetadata("table2",
                    Arrays.asList(IntegerColumnMetadata.builder("column11").notNull().primaryKey().build(),
                            IntegerColumnMetadata.builder("column12").build(),
                            VarcharColumnMetadata.builder("column13", 20).notNull().build())));
        } catch (SqlException e) {
            System.out.println(e.getMessage());
        }

        try {
            Table table1 = sqlManager.getDatabase("DB1").getTable("table1");
            table1.insert(Arrays.asList(10, 20, null));
        } catch (SqlException e) {
            System.out.println(e.getMessage());
        }

        try {
            Table table1 = sqlManager.getDatabase("DB1").getTable("table1");
            table1.insert(Arrays.asList(10, 20, "test1"));
            table1.insert(Arrays.asList(11, 20, "test1"));
            table1.insert(Arrays.asList(10, 20, "test1"));
        } catch (SqlException e) {
            System.out.println(e.getMessage());
        }

        try {
            Table table1 = sqlManager.getDatabase("DB1").getTable("table1");
            table1.insert(Arrays.asList(12, 20, "test1"));
            table1.insert(Arrays.asList(13, 20, "test2"));
            table1.insert(Arrays.asList(15, 21, "test1"));
        } catch (SqlException e) {
            System.out.println(e.getMessage());
        }

        try {
            ResultSet resultSet = sqlManager.select(SelectExpression.builder(
                    BaseTableReference.newTableReference("table1", "DB1"))
                    .addPredicateWithAnd(SelectionPredicate.equals(
                            new ColumnReference("column3"), 10)).build());
            System.out.println(resultSet);
        } catch (SqlException e) {
            System.out.println(e.getMessage());
        }

        try {
            ResultSet resultSet = sqlManager.select(SelectExpression.builder(
                    BaseTableReference.newTableReference("table1", "DB1"))
                    .build());
            System.out.println(resultSet);
        } catch (SqlException e) {
            System.out.println(e.getMessage());
        }

        try {
            ResultSet resultSet = sqlManager.select(SelectExpression.builder(
                    BaseTableReference.newTableReference("table1", "DB1"))
                    .addPredicateWithAnd(SelectionPredicate.equals(
                            new ColumnReference("column3"), "test1")).build());
            System.out.println(resultSet);
        } catch (SqlException e) {
            System.out.println(e.getMessage());
        }


    }
}
