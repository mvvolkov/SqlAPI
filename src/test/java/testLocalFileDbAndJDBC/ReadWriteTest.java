package testLocalFileDbAndJDBC;

import clientImpl.queries.QueryFactory;
import localFileDatabase.client.impl.FileQueryFactory;
import localFileDatabase.server.LocalFileDbServer;
import org.junit.Test;
import sqlapi.exceptions.SqlException;
import sqlapi.server.SqlServer;

import java.io.File;

import static org.junit.Assert.fail;


/**
 * Before the test the database is saved to a file
 */
public class ReadWriteTest extends AbstractTestRunner {

    private final String fileName = "sqlLocalFileDBReadWriteTest.db";

    public ReadWriteTest(SqlServer sqlServer, String database) {
        super(sqlServer, database);
    }

    @Override
    public void setUp() {
        if (!(sqlServer instanceof LocalFileDbServer)) {
            return;
        }
        super.setUp();
        try {
            // save to file
            sqlServer.executeQuery(
                    FileQueryFactory.saveDatabaseToFile(fileName, databaseName));
        } catch (SqlException se) {
            System.out.println(se.getMessage());
            fail();
        }
    }

    @Test
    public void testReadDatabase() {

        if (!(sqlServer instanceof LocalFileDbServer)) {
            return;
        }
        SqlServer sqlServer1 = LocalFileDbServer.getInstance();
        try {
            // read from file and validate
            sqlServer1.executeQuery(
                    FileQueryFactory.readDatabaseFromFile(fileName, databaseName));
            sqlServer1.executeQuery(QueryFactory.validateDatabase(databaseName, tm1,
                    tm2));
        } catch (SqlException e) {
            System.out.println(e.getMessage());
            fail();
        }
    }

    @Override public void tearDown() {
        if (!(sqlServer instanceof LocalFileDbServer)) {
            return;
        }
        // delete file
        File file = new File(fileName);
        if (file.delete()) {
            System.out.println(fileName + " is deleted!");
        } else {
            System.out.println("Delete file operation is failed.");
        }
        super.tearDown();
    }


}
