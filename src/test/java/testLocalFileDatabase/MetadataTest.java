package testLocalFileDatabase;

import org.junit.Test;
import sqlapi.exceptions.NoSuchDatabaseException;
import sqlapi.metadata.*;

import java.util.Collection;

import static org.junit.Assert.*;

/**
 * Before each test we have two tables:
 * <p>
 * SELECT * FROM DB1.table1;
 * <p>
 * column1, column2, column3, column4
 * 10, 30, test1, t21
 * 11, 31, test2, null
 * 12, 32, test3, t43
 * 13, 33, test2, t653
 * 15, 34, test1, null
 * 16, null, test1, null
 * <p>
 * <p>
 * SELECT * FROM DB1.table2;
 * <p>
 * column5, column3
 * 22, test3
 * 23, test2
 * 25, test4
 */
public class MetadataTest extends AbstractLocalFileDatabaseTest {

    @Test
    public void testDatabases() {
        Collection<String> databases = sqlServer.getDatabases();
        assertEquals(1, databases.size());
        assertEquals("DB1", databases.iterator().next());
    }

    @Test
    public void testTables() {
        try {
            Collection<TableMetadata> tables = sqlServer.getTables("DB1");
            assertEquals(tables.size(), 2);
            TableMetadata table1 = null;
            TableMetadata table2 = null;
            for (TableMetadata table : tables) {
                if (table.getTableName().equals("table1")) {
                    table1 = table;
                }
                if (table.getTableName().equals("table2")) {
                    table2 = table;
                }
            }
            assertNotNull(table1);
            assertNotNull(table2);
            checkTable1(table1);
            checkTable2(table2);


        } catch (NoSuchDatabaseException e) {
            System.out.println(e.getMessage());
            fail();
        }
    }

    /**
     * CREATE TABLE DB1.table1(column1 INTEGER PRIMARY KEY, column2 INTEGER DEFAULT 15,
     * column3 VARCHAR(20) NOT NULL, column4 VARCHAR(5));
     */
    private static void checkTable1(TableMetadata table) {
        assertEquals(4, table.getColumnsMetadata().size());
        ColumnMetadata column1 = null;
        ColumnMetadata column2 = null;
        ColumnMetadata column3 = null;
        ColumnMetadata column4 = null;
        for (ColumnMetadata column : table.getColumnsMetadata()) {
            if (column.getColumnName().equals("column1")) {
                column1 = column;
            }
            if (column.getColumnName().equals("column2")) {
                column2 = column;
            }
            if (column.getColumnName().equals("column3")) {
                column3 = column;
            }
            if (column.getColumnName().equals("column4")) {
                column4 = column;
            }
        }
        assertNotNull(column1);
        assertSame(SqlType.INTEGER, column1.getSqlType());
        assertEquals(1, column1.getConstraints().size());
        assertTrue(columnHasConstraint(column1, ColumnConstraintType.PRIMARY_KEY));
        assertNotNull(column2);
        assertSame(SqlType.INTEGER, column2.getSqlType());
        assertEquals(1, column2.getConstraints().size());
        assertTrue(columnHasConstraint(column2, ColumnConstraintType.DEFAULT_VALUE));
        assertEquals(15, getColumnDefaultValue(column2));
        assertNotNull(column3);
        assertSame(SqlType.VARCHAR, column3.getSqlType());
        assertEquals(2, column3.getConstraints().size());
        assertTrue(columnHasConstraint(column3, ColumnConstraintType.NOT_NULL));
        assertTrue(columnHasConstraint(column3, ColumnConstraintType.MAX_SIZE));
        assertEquals(20, getColumnMaxSize(column3));
        assertNotNull(column4);
        assertSame(SqlType.VARCHAR, column4.getSqlType());
        assertEquals(1, column4.getConstraints().size());
        assertTrue(columnHasConstraint(column4, ColumnConstraintType.MAX_SIZE));
        assertEquals(5, getColumnMaxSize(column4));
    }

    /**
     * CREATE TABLE DB1.table2(column5 INTEGER PRIMARY KEY, column3 VARCHAR(15));
     */
    private static void checkTable2(TableMetadata table) {
        assertEquals(2, table.getColumnsMetadata().size());
        ColumnMetadata column1 = null;
        ColumnMetadata column2 = null;
        for (ColumnMetadata column : table.getColumnsMetadata()) {
            if (column.getColumnName().equals("column5")) {
                column1 = column;
            }
            if (column.getColumnName().equals("column3")) {
                column2 = column;
            }
        }
        assertNotNull(column1);
        assertSame(SqlType.INTEGER, column1.getSqlType());
        assertEquals(1, column1.getConstraints().size());
        assertTrue(columnHasConstraint(column1, ColumnConstraintType.PRIMARY_KEY));
        assertNotNull(column2);
        assertSame(SqlType.VARCHAR, column2.getSqlType());
        assertEquals(1, column2.getConstraints().size());
        assertTrue(columnHasConstraint(column2, ColumnConstraintType.MAX_SIZE));
        assertEquals(15, getColumnMaxSize(column2));
    }

    private static boolean columnHasConstraint(ColumnMetadata column,
                                               ColumnConstraintType type) {
        for (ColumnConstraint constraint : column.getConstraints()) {
            if (constraint.getConstraintType() == type) {
                return true;
            }
        }
        return false;
    }

    private static int getColumnMaxSize(ColumnMetadata column) {
        for (ColumnConstraint constraint : column.getConstraints()) {
            if (constraint.getConstraintType() == ColumnConstraintType.MAX_SIZE) {
                return (int) constraint.getParameters().get(0);
            }
        }
        return -1;
    }

    private static Object getColumnDefaultValue(ColumnMetadata column) {
        for (ColumnConstraint constraint : column.getConstraints()) {
            if (constraint.getConstraintType() == ColumnConstraintType.DEFAULT_VALUE) {
                return constraint.getParameters().get(0);
            }
        }
        return null;
    }
}
