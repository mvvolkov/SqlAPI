package testSimpleFileImpl;

import SqlManagerFactory.SqlManagerFactory;
import sqlapi.*;
import sqlapi.exceptions.DatabaseAlreadyExistsException;
import sqlapi.exceptions.NoSuchDatabaseException;
import sqlapi.exceptions.NoSuchTableException;
import sqlapi.exceptions.TableAlreadyExistsException;
import sqlapi.selectionPredicate.SelectionPredicate;
import sqlapi.selectionResult.ResultSet;


import java.util.Arrays;

public class TestSimplePrintOutImpl {

    public static void main(String[] args) {

        SqlManager sqlManager = SqlManagerFactory.getPrintOutSqlManager();

        try {
            sqlManager.createDatabase("DB1");
        } catch (DatabaseAlreadyExistsException e) {
            System.out.println(e.getMessage());
        }

        try {
            sqlManager.createDatabase("DB1");
        } catch (DatabaseAlreadyExistsException e) {
            System.out.println(e.getMessage());
        }

        try {
            sqlManager.getDatabase("DB1").createTable(new TableMetadataImpl("table1",
                    Arrays.asList(new IntegerColumnMetadataImpl.Builder("column1").notNull().primaryKey().build(),
                            new IntegerColumnMetadataImpl.Builder("column2").build(),
                            new VarcharColumnMetadataImpl.Builder("column3", 20).notNull().build())));
        } catch (NoSuchDatabaseException e) {
            System.out.println(e.getMessage());
        } catch (TableAlreadyExistsException e) {
            System.out.println(e.getMessage());
        }

        try {
            sqlManager.getDatabase("DB1").getTable("table2").insert(Arrays.asList(10,
                    null, "test"));
        } catch (NoSuchDatabaseException e) {
            System.out.println(e.getMessage());
        } catch (NoSuchTableException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            sqlManager.getDatabase("DB1").getTable("table1").insert(Arrays.asList("column1", "column2", "column3"), Arrays.asList(10,
                    null, "test"));
        } catch (NoSuchDatabaseException e) {
            System.out.println(e.getMessage());
        } catch (NoSuchTableException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            sqlManager.getDatabase("DB1").getTable("table1").delete(SelectionPredicate.empty());
        } catch (NoSuchDatabaseException e) {
            System.out.println(e.getMessage());
        } catch (NoSuchTableException e) {
            System.out.println(e.getMessage());
        }
        try {
            sqlManager.getDatabase("DB1").getTable("table1").
                    delete(SelectionPredicate.and(SelectionPredicate.or(SelectionPredicate.equals(new ColumnReference("column1"), 3),
                            SelectionPredicate.greaterThan(new ColumnReference("column2"), "12")),
                            SelectionPredicate.isNull(new ColumnReference("column3")))
                    );
        } catch (NoSuchDatabaseException e) {
            System.out.println(e.getMessage());
        } catch (NoSuchTableException e) {
            System.out.println(e.getMessage());
        }

        try {
            sqlManager.getDatabase("DB1").getTable("table1").
                    update(Arrays.asList(new AssignmentOperation("column1", 10),
                            new AssignmentOperation("column2", "test3")),
                            SelectionPredicate.lessThan(new ColumnReference("column3"), "abs"));

            sqlManager.getDatabase("DB1").getTable("table1").
                    update(Arrays.asList(new AssignmentOperation("column1", 10),
                            new AssignmentOperation("column2", "test3")),
                            SelectionPredicate.in(new ColumnReference("column3"), Arrays.asList("12", "13", "14")));
        } catch (NoSuchTableException e) {
            e.printStackTrace();
        } catch (NoSuchDatabaseException e) {
            e.printStackTrace();
        }

        try {
            ResultSet resultSet = sqlManager.getDatabase("DB1").getTable("table1").
                    select(Arrays.asList(SelectedColumn.all()), SelectionPredicate.empty());
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