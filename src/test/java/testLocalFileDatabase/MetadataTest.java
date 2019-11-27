package testLocalFileDatabase;

import org.junit.Test;
import sqlapi.exceptions.NoSuchDatabaseException;
import sqlapi.metadata.TableMetadata;

import java.util.Collection;

import static org.junit.Assert.*;

public class MetadataTest extends AbstractServerLocalFileTest {

    @Test
    public void testDatabases() {
        Collection<String> databases = sqlServer.getDatabases();
        assertEquals(databases.size(), 1);
        assertEquals(databases.iterator().next(), "DB1");
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

    private static void checkTable1(TableMetadata table) {

    }

    private static void checkTable2(TableMetadata table) {

    }

}
