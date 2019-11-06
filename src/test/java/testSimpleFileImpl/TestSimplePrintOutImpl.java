package testSimpleFileImpl;

import clientDefaultImpl.*;
import sqlFactory.SqlManagerFactory;
import api.*;
import api.exceptions.DatabaseAlreadyExistsException;
import api.exceptions.NoSuchDatabaseException;
import api.exceptions.NoSuchTableException;
import api.exceptions.TableAlreadyExistsException;
import api.selectionResult.ResultSet;


import java.util.Arrays;

public class TestSimplePrintOutImpl {

    public static void main(String[] args) {

        SqlServer sqlServer = SqlManagerFactory.getPrintOutSqlManager();
        SqlClient sqlClient = new SqlClientImpl();

        try {
            sqlServer.createDatabase("DB1");
        } catch (DatabaseAlreadyExistsException e) {
            System.out.println(e.getMessage());
        }

        try {
            sqlServer.createDatabase("DB1");
        } catch (DatabaseAlreadyExistsException e) {
            System.out.println(e.getMessage());
        }

        try {
            sqlServer.getDatabase("DB1").createTable(new TableMetadataImpl("table1",
                    Arrays.asList(new IntegerColumnMetadataImpl.Builder("column1").notNull().primaryKey().build(),
                            new IntegerColumnMetadataImpl.Builder("column2").build(),
                            new VarcharColumnMetadataImpl.Builder("column3", 20).notNull().build())));
        } catch (NoSuchDatabaseException e) {
            System.out.println(e.getMessage());
        } catch (TableAlreadyExistsException e) {
            System.out.println(e.getMessage());
        }

        try {
            sqlServer.getDatabase("DB1").getTable("table2").insert(Arrays.asList(10,
                    null, "test"));
        } catch (NoSuchDatabaseException e) {
            System.out.println(e.getMessage());
        } catch (NoSuchTableException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            sqlServer.getDatabase("DB1").getTable("table1").insert(Arrays.asList("column1", "column2", "column3"), Arrays.asList(10,
                    null, "test"));
        } catch (NoSuchDatabaseException e) {
            System.out.println(e.getMessage());
        } catch (NoSuchTableException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            sqlServer.getDatabase("DB1").getTable("table1").delete(sqlClient.getPredicateEmpty());
        } catch (NoSuchDatabaseException e) {
            System.out.println(e.getMessage());
        } catch (NoSuchTableException e) {
            System.out.println(e.getMessage());
        }
        try {
            sqlServer.getDatabase("DB1").getTable("table1").
                    delete(sqlClient.getPredicateAnd(sqlClient.getPredicateOr(
                            sqlClient.getPredicateEquals(new ColumnReference("column1"), 3),
                            sqlClient.getPredicateGreaterThan(new ColumnReference("column2"), "12")),
                            sqlClient.getPredicateIsNull(new ColumnReference("column3")))
                    );
        } catch (NoSuchDatabaseException e) {
            System.out.println(e.getMessage());
        } catch (NoSuchTableException e) {
            System.out.println(e.getMessage());
        }

        try {
            sqlServer.getDatabase("DB1").getTable("table1").
                    update(Arrays.asList(new AssignmentOperation("column1", 10),
                            new AssignmentOperation("column2", "test3")),
                            sqlClient.getPredicateLessThan(new ColumnReference("column3"), "abs"));

            sqlServer.getDatabase("DB1").getTable("table1").
                    update(Arrays.asList(new AssignmentOperation("column1", 10),
                            new AssignmentOperation("column2", "test3")),
                            sqlClient.getPredicateIn(new ColumnReference("column3"), Arrays.asList("12", "13", "14")));
        } catch (NoSuchTableException e) {
            e.printStackTrace();
        } catch (NoSuchDatabaseException e) {
            e.printStackTrace();
        }

        try {
            ResultSet resultSet = sqlServer.getDatabase("DB1").getTable("table1").
                    select(Arrays.asList(SelectedColumn.all()), sqlClient.getPredicateEmpty());
            System.out.println(resultSet);
        } catch (NoSuchTableException e) {
            e.printStackTrace();
        } catch (NoSuchDatabaseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
