package testServerLocalFileImpl;

import sqlapi.server.SqlServer;
import sqlapi.exceptions.SqlException;
import sqlapi.selectResult.ResultRow;
import sqlapi.selectResult.ResultSet;
import clientImpl.metadata.MetadataFactory;
import clientImpl.queries.SqlQueryFactory;
import clientImpl.tableRef.TableRefFactory;
import org.junit.Before;
import ServerFactory.SqlManagerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public abstract class AbstractServerLocalFileTest {

    SqlServer sqlServer;

    @Before
    public void setUp() {
        System.out.println("===== SET UP =====");
        sqlServer = SqlManagerFactory.getServerLocalFileSqlManager();
        try {
            // Create a database
            sqlServer.createDatabase("DB1");
            sqlServer.createSchema("DB1", "MySchema");
            sqlServer.setCurrentSchema("DB1", "MySchema");

            // Create a table
            sqlServer.executeStatement(SqlQueryFactory
                    .createTable("DB1", "table1", Arrays.asList(
                            MetadataFactory.integerBuilder("column1").notNull()
                                    .primaryKey().build(),
                            MetadataFactory.integerBuilder("column2")
                                    .defaultValue(15)
                                    .build(),
                            MetadataFactory.varcharBuilder("column3", 20)
                                    .notNull()
                                    .build(),
                            MetadataFactory.varcharBuilder("column4", 5).build()
                    )));

            // Fill table1
            sqlServer.executeStatement(SqlQueryFactory.insert("DB1", "table1",
                    Arrays.asList(10, 30, "test1", "t21")));
            sqlServer.executeStatement(SqlQueryFactory.insert("DB1", "table1",
                    Arrays.asList(11, 31, "test2", null)));
            sqlServer.executeStatement(SqlQueryFactory.insert("DB1", "table1",
                    Arrays.asList(12, 32, "test3", "t43")));
            sqlServer.executeStatement(SqlQueryFactory.insert("DB1", "table1",
                    Arrays.asList(13, 33, "test2", "t653")));
            sqlServer.executeStatement(SqlQueryFactory.insert("DB1", "table1",
                    Arrays.asList(15, 34, "test1", null)));
            sqlServer.executeStatement(SqlQueryFactory.insert("DB1", "table1",
                    Arrays.asList(16, null, "test1", null)));

            // Create a table
            sqlServer.executeStatement(
                    SqlQueryFactory.createTable("DB1", "table2",
                            Arrays.asList(
                                    MetadataFactory.integerBuilder("column5")
                                            .notNull().primaryKey().build(),
                                    MetadataFactory.varcharBuilder("column3", 15)
                                            .build()
                            )));

            // Fill table2
            sqlServer.executeStatement(
                    SqlQueryFactory.insert("DB1", "table2", Arrays.asList(22, "test3")));
            sqlServer.executeStatement(
                    SqlQueryFactory.insert("DB1", "table2", Arrays.asList(23, "test2")));
            sqlServer.executeStatement(
                    SqlQueryFactory.insert("DB1", "table2", Arrays.asList(25, "test4")));

            System.out.println("===== END OF SET UP =====");

        } catch (SqlException e) {
            System.out.println(e.getMessage());
        }
    }

    protected ResultSet getTableData(String dbName, String tableName)
            throws SqlException {
        return sqlServer.getQueryResult(
                SqlQueryFactory.select(TableRefFactory.dbTable(dbName, tableName)));
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
                Object resultValue = row.getObject(i);

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
                Arrays.stream(values).map(obj -> String.valueOf(obj)).collect(
                        Collectors.joining(", ", "{", "}")));

    }
}
