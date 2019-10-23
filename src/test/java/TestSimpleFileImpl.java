import SqlManagerFactory.SqlManagerFactory;
import sqlapi.*;
import sqlapi.dbMetadata.IntegerColumnMetadata;
import sqlapi.dbMetadata.TableMetadata;
import sqlapi.dbMetadata.VarcharColumnMetadata;
import sqlapi.exceptions.*;

import java.util.Arrays;

public class TestSimpleFileImpl {
    public static void main(String[] args) {

        SqlManager sqlManager = SqlManagerFactory.getSimpleFileSqlManager();

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
            sqlManager.getDatabase("DB1").createTable(new TableMetadata("table1",
                    Arrays.asList(IntegerColumnMetadata.builder("column1").notNull().primaryKey().build(),
                            IntegerColumnMetadata.builder("column2").build(),
                            VarcharColumnMetadata.builder("column3", 20).notNull().build())));
        } catch (NoSuchDatabaseException e) {
            System.out.println(e.getMessage());
        } catch (TableAlreadyExistsException e) {
            System.out.println(e.getMessage());
        }

        try {
            Table table1 = sqlManager.getDatabase("DB1").getTable("table1");
            table1.insert(Arrays.asList(10, 20, "test1"));
            table1.insert(Arrays.asList(11, 20, "test2"));
        } catch (NoSuchDatabaseException e) {
            System.out.println(e.getMessage());
        } catch (NoSuchTableException e) {
            System.out.println(e.getMessage());
        } catch (WrongValueTypeException e) {
            System.out.println(e.getMessage());
        } catch (ConstraintException e) {
            System.out.println(e.getMessage());
        }

//        try {
//            sqlManager.getDatabase("DB1").getTable("table1").insert(Arrays.asList("column1", "column2", "column3"), Arrays.asList(10,
//                    null, "test"));
//        } catch (NoSuchDatabaseException e) {
//            System.out.println(e.getMessage());
//        } catch (NoSuchTableException e) {
//            System.out.println(e.getMessage());
//        }
//
//        try {
//            sqlManager.getDatabase("DB1").getTable("table1").delete(SelectionCriteria.empty());
//        } catch (NoSuchDatabaseException e) {
//            System.out.println(e.getMessage());
//        } catch (NoSuchTableException e) {
//            System.out.println(e.getMessage());
//        }
//        try {
//            sqlManager.getDatabase("DB1").getTable("table1").
//                    delete(SelectionCriteria.and(SelectionCriteria.or(SelectionCriteria.equals(new ColumnReference("column1"), 3),
//                            SelectionCriteria.greaterThan(new ColumnReference("column2"), "12")),
//                            SelectionCriteria.isNull(new ColumnReference("column3")))
//                    );
//        } catch (NoSuchDatabaseException e) {
//            System.out.println(e.getMessage());
//        } catch (NoSuchTableException e) {
//            System.out.println(e.getMessage());
//        }
//
//        try {
//            sqlManager.getDatabase("DB1").getTable("table1").
//                    update(Arrays.asList(new AssignmentOperation("column1", 10),
//                            new AssignmentOperation("column2", "test3")),
//                            SelectionCriteria.lessThan(new ColumnReference("column3"), "abs"));
//
//            sqlManager.getDatabase("DB1").getTable("table1").
//                    update(Arrays.asList(new AssignmentOperation("column1", 10),
//                            new AssignmentOperation("column2", "test3")),
//                            SelectionCriteria.in(new ColumnReference("column3"), Arrays.asList("12", "13", "14")));
//        } catch (NoSuchTableException e) {
//            e.printStackTrace();
//        } catch (NoSuchDatabaseException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            sqlManager.getDatabase("DB1").getTable("table1").
//                    select(Arrays.asList(SelectionUnit.all()), SelectionCriteria.empty());
//        } catch (NoSuchTableException e) {
//            e.printStackTrace();
//        } catch (NoSuchDatabaseException e) {
//            e.printStackTrace();
//        }
    }
}
