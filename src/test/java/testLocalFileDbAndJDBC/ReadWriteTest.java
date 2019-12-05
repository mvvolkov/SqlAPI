package testLocalFileDbAndJDBC;

import clientImpl.columnExpr.ColumnExprFactory;
import localFileDatabase.client.impl.FileQueryFactory;
import clientImpl.metadata.MetadataFactory;
import clientImpl.queries.QueryFactory;
import clientImpl.tables.TableRefFactory;
import localFileDatabase.server.LocalFileDbServer;
import org.junit.Test;
import sqlapi.exceptions.SqlException;
import sqlapi.metadata.TableMetadata;
import sqlapi.queryResult.QueryResult;
import sqlapi.server.SqlServer;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


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
public class ReadWriteTest extends AbstractTestRunner {

    public ReadWriteTest(SqlServer sqlServer, String database) {
        super(sqlServer, database);
    }

    @Test
    public void testReadAndWrite() {

        if (!(sqlServer instanceof LocalFileDbServer)) {
            return;
        }
        SqlServer sqlServer = LocalFileDbServer.getInstance();
        SqlServer sqlServer1 = LocalFileDbServer.getInstance();

        try {

            sqlServer.executeQuery(QueryFactory.createDatabase("DB1"));

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
            sqlServer.executeQuery(QueryFactory
                    .createTable("DB1", tm1));

            // Fill table1
            sqlServer.executeQuery(QueryFactory.insert("DB1", "table1",
                    ColumnExprFactory.values(10, 30, "test1", "t21")));
            sqlServer.executeQuery(QueryFactory.insert("DB1", "table1",
                    ColumnExprFactory.values(11, 31, "test2", null)));
            sqlServer.executeQuery(QueryFactory.insert("DB1", "table1",
                    ColumnExprFactory.values(12, 32, "test3", "t43")));
            sqlServer.executeQuery(QueryFactory.insert("DB1", "table1",
                    ColumnExprFactory.values(13, 33, "test2", "t653")));
            sqlServer.executeQuery(QueryFactory.insert("DB1", "table1",
                    ColumnExprFactory.values(15, 34, "test1", null)));
            sqlServer.executeQuery(QueryFactory.insert("DB1", "table1",
                    ColumnExprFactory.values(16, null, "test1", null)));

            TableMetadata tm2 = MetadataFactory.tableMetadata("table2",
                    Arrays.asList(
                            MetadataFactory.integer("column5", Collections
                                    .singletonList(MetadataFactory
                                            .primaryKey())),
                            MetadataFactory.varchar("column3", 15)
                    ));

            // Create a table
            sqlServer.executeQuery(QueryFactory.createTable("DB1", tm2));

            // Fill table2
            sqlServer.executeQuery(
                    QueryFactory.insert("DB1", "table2",
                            ColumnExprFactory.values(22, "test3")));
            sqlServer.executeQuery(
                    QueryFactory.insert("DB1", "table2",
                            ColumnExprFactory.values(23, "test2")));
            sqlServer.executeQuery(
                    QueryFactory.insert("DB1", "table2",
                            ColumnExprFactory.values(25, "test4")));


            // save databases
//            String tempDir = "C:\\Users\\mvvol\\IdeaProjects\\";
            String tempDir = System.getProperty("java.io.tmpdir");
            String fileName = tempDir + "mpsReadWriteTest1";
            sqlServer.executeQuery(FileQueryFactory.saveDatabase(fileName, "DB1"));

            sqlServer1.executeQuery(FileQueryFactory
                    .readDatabase(fileName, "DB1", Arrays.asList(tm1, tm2)));

            QueryResult queryResult = sqlServer1.getQueryResult(
                    QueryFactory.select(TableRefFactory.dbTable("DB1", "table1")));

            printResultSet(queryResult);

            checkHeaders(queryResult.getHeaders(), "column1", "column2", "column3",
                    "column4");
            assertEquals(6, queryResult.getRows().size());
            checkRowExists(queryResult, 10, 30, "test1", "t21");
            checkRowExists(queryResult, 11, 31, "test2", null);
            checkRowExists(queryResult, 12, 32, "test3", "t43");
            checkRowExists(queryResult, 13, 33, "test2", "t653");
            checkRowExists(queryResult, 15, 34, "test1", null);
            checkRowExists(queryResult, 16, null, "test1", null);

        } catch (SqlException se) {
            System.out.println(se.getMessage());
            fail();
        }
    }
}
