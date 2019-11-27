package testLocalFileDatabase;

import clientImpl.metadata.MetadataFactory;
import clientImpl.queries.QueryFactory;
import clientImpl.tables.TableRefFactory;
import org.junit.Before;
import LocalFileDatabase.server.LocalFileDatabaseServerFactory;
import sqlapi.exceptions.SqlException;
import sqlapi.queryResult.ResultRow;
import sqlapi.queryResult.ResultSet;
import sqlapi.server.SqlServer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public abstract class AbstractServerLocalFileTest {

    SqlServer sqlServer;

    @Before
    public void setUp() {
        System.out.println("===== SET UP =====");
        sqlServer = LocalFileDatabaseServerFactory.getServer();
        try {
            // Create a database
            sqlServer.executeQuery(QueryFactory.createDatabase("DB1"));

            // Create a table
            sqlServer.executeQuery(QueryFactory
                    .createTable("DB1",
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
            sqlServer.executeQuery(QueryFactory.insert("DB1", "table1",
                    Arrays.asList(10, 30, "test1", "t21")));
            sqlServer.executeQuery(QueryFactory.insert("DB1", "table1",
                    Arrays.asList(11, 31, "test2", null)));
            sqlServer.executeQuery(QueryFactory.insert("DB1", "table1",
                    Arrays.asList(12, 32, "test3", "t43")));
            sqlServer.executeQuery(QueryFactory.insert("DB1", "table1",
                    Arrays.asList(13, 33, "test2", "t653")));
            sqlServer.executeQuery(QueryFactory.insert("DB1", "table1",
                    Arrays.asList(15, 34, "test1", null)));
            sqlServer.executeQuery(QueryFactory.insert("DB1", "table1",
                    Arrays.asList(16, null, "test1", null)));

            // Create a table
            sqlServer.executeQuery(
                    QueryFactory
                            .createTable("DB1", MetadataFactory.tableMetadata("table2",
                                    Arrays.asList(
                                            MetadataFactory.integer("column5", Collections
                                                    .singletonList(MetadataFactory
                                                            .primaryKey())),
                                            MetadataFactory.varchar("column3", 15)
                                    ))));

            // Fill table2
            sqlServer.executeQuery(
                    QueryFactory.insert("DB1", "table2", Arrays.asList(22, "test3")));
            sqlServer.executeQuery(
                    QueryFactory.insert("DB1", "table2", Arrays.asList(23, "test2")));
            sqlServer.executeQuery(
                    QueryFactory.insert("DB1", "table2", Arrays.asList(25, "test4")));

            System.out.println("===== END OF SET UP =====");

        } catch (SqlException e) {
            System.out.println(e.getMessage());
            fail();
        }
    }

    ResultSet getTableData(String dbName, String tableName)
            throws SqlException {
        ResultSet resultSet = sqlServer.getQueryResult(
                QueryFactory.select(TableRefFactory.dbTable(dbName, tableName)));
        printResultSet(resultSet);
        return resultSet;
    }

    static void printResultSet(ResultSet resultSet) {

        StringBuilder sb = new StringBuilder();

        String columnsHeaders = String.join(", ", resultSet.getHeaders());

        sb.append(columnsHeaders);
        for (ResultRow row : resultSet.getRows()) {
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

    static void checkRowExists(ResultSet resultSet, Object... values) {
        assertEquals(resultSet.getHeaders().size(), values.length);

        for (ResultRow row : resultSet.getRows()) {
            boolean rowMatch = true;
            for (int i = 0; i < resultSet.getHeaders().size(); i++) {
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
