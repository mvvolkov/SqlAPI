package testServerLocalFileImpl;

import clientImpl.metadata.MetadataFactory;
import clientImpl.queries.SqlQueryFactory;
import clientImpl.tableRef.TableRefFactory;
import org.junit.Test;
import serverLocalFileImpl.SqlLocalFileServerFactory;
import serverLocalFileImpl.SqlServerLocalFile;
import sqlapi.exceptions.SqlException;
import sqlapi.metadata.TableMetadata;
import sqlapi.selectResult.ResultSet;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ReadWriteTest extends AbstractServerLocalFileTest {

    @Test
    public void test1() {
        SqlServerLocalFile sqlServer = SqlLocalFileServerFactory.getServer();
        SqlServerLocalFile sqlServer1 = SqlLocalFileServerFactory.getServer();

        try {

            sqlServer.createDatabase("DB1");

            TableMetadata tm1 = MetadataFactory.tableMetadata("table1", Arrays.asList(
                    MetadataFactory.integer("column1",
                            Collections.singletonList(MetadataFactory.primaryKey())),
                    MetadataFactory.integer("column2", Collections
                            .singletonList(
                                    MetadataFactory.defaultVal(15))),
                    MetadataFactory.varchar("column3", 20,
                            Collections.singletonList(
                                    MetadataFactory.notNull())),
                    MetadataFactory.varchar("column4", 5)
            ));

            // Create a table
            sqlServer.executeQuery(SqlQueryFactory
                    .createTable("DB1", tm1));

            // Fill table1
            sqlServer.executeQuery(SqlQueryFactory.insert("DB1", "table1",
                    Arrays.asList(10, 30, "test1", "t21")));
            sqlServer.executeQuery(SqlQueryFactory.insert("DB1", "table1",
                    Arrays.asList(11, 31, "test2", null)));
            sqlServer.executeQuery(SqlQueryFactory.insert("DB1", "table1",
                    Arrays.asList(12, 32, "test3", "t43")));
            sqlServer.executeQuery(SqlQueryFactory.insert("DB1", "table1",
                    Arrays.asList(13, 33, "test2", "t653")));
            sqlServer.executeQuery(SqlQueryFactory.insert("DB1", "table1",
                    Arrays.asList(15, 34, "test1", null)));
            sqlServer.executeQuery(SqlQueryFactory.insert("DB1", "table1",
                    Arrays.asList(16, null, "test1", null)));

            TableMetadata tm2 = MetadataFactory.tableMetadata("table2",
                    Arrays.asList(
                            MetadataFactory.integer("column5", Collections
                                    .singletonList(MetadataFactory
                                            .primaryKey())),
                            MetadataFactory.varchar("column3", 15)
                    ));

            // Create a table
            sqlServer.executeQuery(SqlQueryFactory.createTable("DB1", tm2));

            // Fill table2
            sqlServer.executeQuery(
                    SqlQueryFactory.insert("DB1", "table2", Arrays.asList(22, "test3")));
            sqlServer.executeQuery(
                    SqlQueryFactory.insert("DB1", "table2", Arrays.asList(23, "test2")));
            sqlServer.executeQuery(
                    SqlQueryFactory.insert("DB1", "table2", Arrays.asList(25, "test4")));


            // save databases
            String tempDir = "C:\\Users\\mvvol\\IdeaProjects\\";
            //System.getProperty("java.io.tmpdir");
            String fileName = tempDir + "mpsReadWriteTest1";
            sqlServer.saveDatabase("DB1", fileName);

            sqlServer1.readDatabase(fileName, "DB1", Arrays.asList(tm1, tm2));

            ResultSet resultSet = sqlServer1.getQueryResult(
                    SqlQueryFactory.select(TableRefFactory.dbTable("DB1", "table1")));

            printResultSet(resultSet);

            checkHeaders(resultSet.getHeaders(), "column1", "column2", "column3",
                    "column4");
            assertEquals(6, resultSet.getRows().size());
            checkRowExists(resultSet, 10, 30, "test1", "t21");
            checkRowExists(resultSet, 11, 31, "test2", null);
            checkRowExists(resultSet, 12, 32, "test3", "t43");
            checkRowExists(resultSet, 13, 33, "test2", "t653");
            checkRowExists(resultSet, 15, 34, "test1", null);
            checkRowExists(resultSet, 16, null, "test1", null);

        } catch (SqlException | IOException | ClassNotFoundException se) {
            System.out.println(se.getMessage());
            fail();
        }

    }
}
