import SqlManagerFactory.SqlManagerFactory;
import api.*;
import api.exceptions.DatabaseAlreadyExistsException;
import api.exceptions.NoSuchDatabaseException;
import api.exceptions.NoSuchTableException;
import api.exceptions.TableAlreadyExistsException;

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
            sqlManager.getDatabase("DB1").createTable(new TableDescription("table1",
                    Arrays.asList(new IntegerColumnDescription.Builder("column1").notNull().primaryKey().build(),
                            new IntegerColumnDescription.Builder("column2").build(),
                            new VarcharColumnDescription.Builder("column3", 20).notNull().build())));
        } catch (NoSuchDatabaseException e) {
            System.out.println(e.getMessage());
        } catch (TableAlreadyExistsException e) {
            System.out.println(e.getMessage());
        }

        try {
            sqlManager.getDatabase("DB1").getTable("table2").insert(Arrays.asList(SqlInsertableValue.integerValueOf(10),
                    SqlInsertableValue.nullValue(), SqlInsertableValue.stringValueOf("test")));
        } catch (NoSuchDatabaseException e) {
            System.out.println(e.getMessage());
        } catch (NoSuchTableException e) {
            System.out.println(e.getMessage());
        }

        try {
            sqlManager.getDatabase("DB1").getTable("table1").insert(Arrays.asList("column1", "column2", "column3"), Arrays.asList(SqlInsertableValue.integerValueOf(10),
                    SqlInsertableValue.nullValue(), SqlInsertableValue.stringValueOf("test")));
        } catch (NoSuchDatabaseException e) {
            System.out.println(e.getMessage());
        } catch (NoSuchTableException e) {
            System.out.println(e.getMessage());
        }

        try {
            sqlManager.getDatabase("DB1").getTable("table1").delete(SqlSelectionCondition.getEmpty());
        } catch (NoSuchDatabaseException e) {
            System.out.println(e.getMessage());
        } catch (NoSuchTableException e) {
            System.out.println(e.getMessage());
        }
        try {
            sqlManager.getDatabase("DB1").getTable("table1").delete(SqlSelectionCondition.getEquals(2, 3));
        } catch (NoSuchDatabaseException e) {
            System.out.println(e.getMessage());
        } catch (NoSuchTableException e) {
            System.out.println(e.getMessage());
        }
    }
}
