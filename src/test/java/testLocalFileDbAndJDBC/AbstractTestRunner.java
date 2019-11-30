package testLocalFileDbAndJDBC;

import clientImpl.columnExpr.ColumnExprFactory;
import clientImpl.metadata.MetadataFactory;
import clientImpl.queries.QueryFactory;
import clientImpl.tables.TableRefFactory;
import localFileDatabase.server.LocalFileDatabaseServerFactory;
import mySqlJdbcServer.MySQL_JDBC_Server;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import sqlapi.exceptions.SqlException;
import sqlapi.queryResult.QueryResultRow;
import sqlapi.queryResult.QueryResult;
import sqlapi.server.SqlServer;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(Parameterized.class)
public abstract class AbstractTestRunner {

    protected SqlServer sqlServer;

    protected static String databaseName = "test2019";

    public AbstractTestRunner(SqlServer sqlServer) {
        this.sqlServer = sqlServer;
    }
    
    @Parameterized.Parameters
    public static Collection<SqlServer> getServers() {

        Collection<SqlServer> servers = new ArrayList<>();

        SqlServer localFileDbServer = LocalFileDatabaseServerFactory.getServer();
        try {
            localFileDbServer.executeQuery(QueryFactory.createDatabase(databaseName));
            servers.add(localFileDbServer);
        } catch (SqlException ignored) {
        }
        SqlServer mySqlServer = new MySQL_JDBC_Server(
                "jdbc:mysql://localhost:3306/test2019?useUnicode=true" +
                        "&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode" +
                        "=false&serverTimezone=Europe/Moscow",
                "root", "mpsjetbrains2019");
        servers.add(mySqlServer);
        return servers;
    }


    @Before
    public void setUp() {
        System.out.println("===== SET UP =====");
        try {
            sqlServer.connect();
            this.createTable1();
            this.createTable2();
        } catch (SqlException e) {
            System.out.println(e.getMessage());
            fail();
        } finally {
            System.out.println("===== END OF SET UP =====");
        }
    }

    @After
    public void tearDown() {
        try {
            sqlServer.executeQuery(QueryFactory.dropTable(databaseName, "table1"));
            sqlServer.executeQuery(QueryFactory.dropTable(databaseName, "table2"));
            sqlServer.close();
        } catch (SqlException e) {
            System.out.println(e.getMessage());
        }
    }

    private void createTable1() throws SqlException {
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
    }

    private void createTable2() throws SqlException {
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
                if (value instanceof Number) {
                    if (!(resultValue instanceof Number)) {
                        rowMatch = false;
                        break;
                    }
                    BigDecimal b1 = new BigDecimal(((Number) value).doubleValue());
                    BigDecimal b2 = new BigDecimal(((Number) resultValue).doubleValue());
                    if (b1.compareTo(b2) != 0) {
                        rowMatch = false;
                        break;
                    }
                } else {
                    if (!value.equals(resultValue)) {
                        rowMatch = false;
                        break;
                    }
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
