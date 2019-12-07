package testLocalFileDbAndJDBC;

import clientImpl.columnExpr.ColumnExprFactory;
import clientImpl.metadata.MetadataFactory;
import clientImpl.queries.QueryFactory;
import clientImpl.tables.TableRefFactory;
import localFileDatabase.server.LocalFileDbServer;
import mySQL_JDBC_Server.MySQL_JDBC_Server;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import sqlapi.exceptions.SqlException;
import sqlapi.metadata.TableMetadata;
import sqlapi.queries.InsertQuery;
import sqlapi.queryResult.QueryResult;
import sqlapi.queryResult.QueryResultRow;
import sqlapi.server.SqlServer;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(Parameterized.class)
public abstract class AbstractTestRunner {

    protected final SqlServer sqlServer;

    protected final String databaseName;

    protected final TableMetadata tm1;

    protected final TableMetadata tm2;

    public AbstractTestRunner(SqlServer sqlServer, String databaseName) {
        this.sqlServer = sqlServer;
        this.databaseName = databaseName;

        tm1 = MetadataFactory.table("table1",
                MetadataFactory.integer("column1", MetadataFactory.primaryKey()),
                MetadataFactory
                        .integer("column2", MetadataFactory.defaultVal(15)),
                MetadataFactory.varchar("column3", 20, MetadataFactory.notNull()),
                MetadataFactory.varchar("column4", 5));

        tm2 = MetadataFactory.table("table2", MetadataFactory.integer("column5",
                MetadataFactory.primaryKey()),
                MetadataFactory.varchar("column3", 15));
    }

    @Parameterized.Parameters
    public static Iterable<Object[]> getServers() {

        Collection<Object[]> servers = new ArrayList<>();

        // local SQL server
        servers.add(new Object[]{LocalFileDbServer.getInstance(), "DB1"});

        // free online MySQL server
        servers.add(new Object[]{new MySQL_JDBC_Server(
                "jdbc:mysql://sql7.freesqldatabase.com:3306",
                "sql7314024", "9hc9cPjLjg"), "sql7314024"});

//        // free online MySQL server
//        servers.add(new Object[]{new MySQL_JDBC_Server(
//                "jdbc:mysql://db4free.net:3306",
//                "mvolkov_test2019", "mpsjetbrains2019"), "mvolkov_test2019"});

//        // local MySQL server
//        servers.add(new Object[]{new MySQL_JDBC_Server(
//                "jdbc:mysql://localhost:3306/test2019?useUnicode=true"
//                        + "&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode"
//                        + "=false&serverTimezone=Europe/Moscow",
//                "root", "mpsjetbrains2019"), "test2019"});

        return servers;
    }


    @Before
    public void setUp() {
        System.out.println("===== SET UP =====");
        try {
            sqlServer.connect();
            sqlServer.executeQuery(QueryFactory.createDatabaseIfNotExists(databaseName));
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
        System.out.println("===== TEAR DOWN =====");
        try {
            sqlServer.executeQuery(QueryFactory.dropTable(databaseName, "table1"));
            sqlServer.executeQuery(QueryFactory.dropTable(databaseName, "table2"));
            sqlServer.close();
        } catch (SqlException e) {
            System.out.println(e.getMessage());
        } finally {
            System.out.println("===== END OF TEAR DOWN =====");
        }
    }

    private void createTable1() throws SqlException {
        // Create a table
        sqlServer.executeQuery(QueryFactory.createTable(databaseName, tm1));

        // Fill table1
        InsertQuery insert = QueryFactory.insert(databaseName, "table1",
                ColumnExprFactory.parameter(),
                ColumnExprFactory.parameter(),
                ColumnExprFactory.parameter(),
                ColumnExprFactory.parameter());

        sqlServer.executeQuery(insert, 10, 30, "test1", "t21");
        sqlServer.executeQuery(insert, 11, 31, "test2", null);
        sqlServer.executeQuery(insert, 12, 32, "test3", "t43");
        sqlServer.executeQuery(insert, 13, 33, "test2", "t653");
        sqlServer.executeQuery(insert, 15, 34, "test1", null);
        sqlServer.executeQuery(insert, 16, null, "test1", null);
    }

    private void createTable2() throws SqlException {
        // Create a table
        sqlServer.executeQuery(QueryFactory.createTable(databaseName, tm2));

        // Fill table2
        InsertQuery insert = QueryFactory.insert(databaseName, "table2",
                ColumnExprFactory.parameter(),
                ColumnExprFactory.parameter());
        sqlServer.executeQuery(insert, 22, "test3");
        sqlServer.executeQuery(insert, 23, "test2");
        sqlServer.executeQuery(insert, 25, "test4");
    }


    protected QueryResult getTableData(String dbName, String tableName)
            throws SqlException {
        QueryResult queryResult = sqlServer.getQueryResult(
                QueryFactory.select(TableRefFactory.dbTable(dbName, tableName)));
        printResultSet(queryResult);
        return queryResult;
    }

    protected static void printResultSet(QueryResult queryResult) {

        StringBuilder sb = new StringBuilder();
        sb.append(String.join(", ", queryResult.getHeaders()));
        for (QueryResultRow row : queryResult.getRows()) {
            String rowString =
                    row.getValues().stream().map(String::valueOf)
                            .collect(Collectors.joining(
                                    ", "));
            sb.append("\n").append(rowString);
        }
        System.out.println(sb);
    }

    protected static void checkHeaders(List<String> headers1, String... headers2) {
        assertEquals(headers2.length, headers1.size());
        for (int i = 0; i < headers1.size(); i++) {
            assertEquals(headers2[i], headers1.get(i));
        }
    }

    protected static void checkRowExists(QueryResult queryResult, Object... values) {
        assertEquals(values.length, queryResult.getHeaders().size());

        for (QueryResultRow row : queryResult.getRows()) {
            assertEquals(values.length, row.getValues().size());
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
                    BigDecimal b1 =
                            BigDecimal.valueOf(((Number) value).doubleValue());
                    BigDecimal b2 =
                            BigDecimal.valueOf(((Number) resultValue).doubleValue());
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
