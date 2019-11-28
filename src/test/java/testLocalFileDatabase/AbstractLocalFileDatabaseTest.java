package testLocalFileDatabase;

import clientImpl.columnExpr.ColumnExprFactory;
import clientImpl.metadata.MetadataFactory;
import clientImpl.queries.QueryFactory;
import clientImpl.tables.TableRefFactory;
import junit.framework.TestCase;
import localFileDatabase.server.LocalFileDatabaseServerFactory;
import mySqlJdbcServer.MySqlJdbcServer;
import org.junit.After;
import org.junit.Before;
import sqlapi.exceptions.SqlException;
import sqlapi.queryResult.QueryResultRow;
import sqlapi.queryResult.QueryResult;
import sqlapi.server.SqlServer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public abstract class AbstractLocalFileDatabaseTest {

    SqlServer sqlServer;

    String databaseName = "logiweb";
//    String databaseName = "DB1";


    @Before
    public void setUp() {
        System.out.println("===== SET UP =====");
        sqlServer = LocalFileDatabaseServerFactory.getServer();
        sqlServer = new MySqlJdbcServer();
        try {
            // Create a database
//            sqlServer.executeQuery(QueryFactory.createDatabase(databaseName));

            // Create a table
            sqlServer.executeQuery(QueryFactory
                    .createTable(databaseName,
                            MetadataFactory.tableMetadata("table1", Arrays.asList(
                                    MetadataFactory.integer("column1",
                                            Collections.singletonList(
                                                    MetadataFactory.primaryKey())),
                                    MetadataFactory.integer("column2", Collections
                                            .singletonList(
                                                    MetadataFactory.defaultVal(15))),
                                    MetadataFactory.varchar("column3", 20,
                                            Collections.singletonList(
                                                    MetadataFactory.notNull())),
                                    MetadataFactory.varchar("column4", 5)
                            ))));

            // Fill table1
            sqlServer.executeQuery(QueryFactory.insert(databaseName, "table1",
                    ColumnExprFactory.values(10, 30, "test1", "t21")));
            sqlServer.executeQuery(QueryFactory.insert(databaseName, "table1",
                    ColumnExprFactory.values(11, 31, "test2", null)));
            sqlServer.executeQuery(QueryFactory.insert(databaseName, "table1",
                    ColumnExprFactory.values(12, 32, "test3", "t43")));
            sqlServer.executeQuery(QueryFactory.insert(databaseName, "table1",
                    ColumnExprFactory.values(13, 33, "test2", "t653")));
            sqlServer.executeQuery(QueryFactory.insert(databaseName, "table1",
                    ColumnExprFactory.values(15, 34, "test1", null)));
            sqlServer.executeQuery(QueryFactory.insert(databaseName, "table1",
                    ColumnExprFactory.values(16, null, "test1", null)));

            // Create a table
            sqlServer.executeQuery(
                    QueryFactory
                            .createTable(databaseName,
                                    MetadataFactory.tableMetadata("table2",
                                            Arrays.asList(
                                                    MetadataFactory.integer("column5",
                                                            Collections
                                                                    .singletonList(
                                                                            MetadataFactory
                                                                                    .primaryKey())),
                                                    MetadataFactory.varchar("column3", 15)
                                            ))));

            // Fill table2
            sqlServer.executeQuery(
                    QueryFactory
                            .insert(databaseName, "table2",
                                    ColumnExprFactory.values(22, "test3")));
            sqlServer.executeQuery(
                    QueryFactory
                            .insert(databaseName, "table2",
                                    ColumnExprFactory.values(23, "test2")));
            sqlServer.executeQuery(
                    QueryFactory
                            .insert(databaseName, "table2",
                                    ColumnExprFactory.values(25, "test4")));

            System.out.println("===== END OF SET UP =====");

        } catch (SqlException e) {
            System.out.println(e.getMessage());
            fail();
        }
    }

    @After
    public void tearDown() {
        try {
            sqlServer.executeQuery(QueryFactory.dropTable(databaseName, "table1"));
            sqlServer.executeQuery(QueryFactory.dropTable(databaseName, "table2"));
        } catch (SqlException e) {
            e.printStackTrace();
        }
    }


    QueryResult getTableData(String dbName, String tableName)
            throws SqlException {
        QueryResult queryResult = sqlServer.getQueryResult(
                QueryFactory.select(TableRefFactory.dbTable(dbName, tableName)));
        printResultSet(queryResult);
        return queryResult;
    }

    static void printResultSet(QueryResult queryResult) {

        StringBuilder sb = new StringBuilder();

        String columnsHeaders = String.join(", ", queryResult.getHeaders());

        sb.append(columnsHeaders);
        for (QueryResultRow row : queryResult.getRows()) {
            String rowString =
                    row.getValues().stream().map(String::valueOf)
                            .collect(Collectors.joining(
                                    ", "));
            sb.append("\n").append(rowString);
        }
        System.out.println(sb);
    }

    static void checkHeaders(List<String> headers1, String... headers2) {
        assertEquals(headers1.size(), headers2.length);
        for (int i = 0; i < headers1.size(); i++) {
            assertEquals(headers2[i], headers1.get(i));
        }
    }

    static void checkRowExists(QueryResult queryResult, Object... values) {
        assertEquals(queryResult.getHeaders().size(), values.length);

        for (QueryResultRow row : queryResult.getRows()) {
            boolean rowMatch = true;
            for (int i = 0; i < queryResult.getHeaders().size(); i++) {
                Object value = values[i];
                Object resultValue = row.getValue(i);

                if (value == null) {
                    if (resultValue != null) {
                        rowMatch = false;
                        break;
                    } else {
                        continue;
                    }
                }
                if (!value.equals(resultValue)) {
                    rowMatch = false;
                    break;
                }
            }
            if (rowMatch) {
                return;
            }
        }
        fail("Row not found: " +
                Arrays.stream(values).map(String::valueOf).collect(
                        Collectors.joining(", ", "{", "}")));
    }
}
