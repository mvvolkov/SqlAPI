package testSimpleFileImpl;

import sqlFactory.SqlManagerFactory;
import clientDefaultImpl.IntegerColumnMetadataImpl;
import clientDefaultImpl.TableMetadataImpl;
import clientDefaultImpl.VarcharColumnMetadataImpl;
import api.*;
import api.exceptions.DatabaseAlreadyExistsException;
import api.exceptions.NoSuchDatabaseException;
import api.exceptions.NoSuchTableException;
import api.exceptions.TableAlreadyExistsException;
import clientDefaultImpl.SelectionPredicateImpl;
import api.selectionResult.ResultSet;


import java.util.Arrays;

public class TestSimplePrintOutImpl {

    public static void main(String[] args) {

        SqlServer sqlServer = SqlManagerFactory.getPrintOutSqlManager();

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
            sqlServer.getDatabase("DB1").getTable("table1").delete(SelectionPredicateImpl.empty());
        } catch (NoSuchDatabaseException e) {
            System.out.println(e.getMessage());
        } catch (NoSuchTableException e) {
            System.out.println(e.getMessage());
        }
        try {
            sqlServer.getDatabase("DB1").getTable("table1").
                    delete(SelectionPredicateImpl.and(SelectionPredicateImpl.or(SelectionPredicateImpl.equals(new ColumnReference("column1"), 3),
                            SelectionPredicateImpl.greaterThan(new ColumnReference("column2"), "12")),
                            SelectionPredicateImpl.isNull(new ColumnReference("column3")))
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
                            SelectionPredicateImpl.lessThan(new ColumnReference("column3"), "abs"));

            sqlServer.getDatabase("DB1").getTable("table1").
                    update(Arrays.asList(new AssignmentOperation("column1", 10),
                            new AssignmentOperation("column2", "test3")),
                            SelectionPredicateImpl.in(new ColumnReference("column3"), Arrays.asList("12", "13", "14")));
        } catch (NoSuchTableException e) {
            e.printStackTrace();
        } catch (NoSuchDatabaseException e) {
            e.printStackTrace();
        }

        try {
            ResultSet resultSet = sqlServer.getDatabase("DB1").getTable("table1").
                    select(Arrays.asList(SelectedColumn.all()), SelectionPredicateImpl.empty());
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
