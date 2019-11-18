package testServerLocalFileImpl;

import api.connect.SqlServer;
import api.exceptions.SqlException;
import api.metadata.ColumnMetadata;
import api.selectResult.ResultRow;
import api.selectResult.ResultSet;
import clientImpl.metadata.MetadataFactory;
import clientImpl.queries.SqlQueryFactory;
import clientImpl.tableRef.TableRefFactory;
import org.junit.Before;
import sqlFactory.SqlManagerFactory;

import java.util.Arrays;
import java.util.Map;

import static org.junit.Assert.*;

public abstract class AbstractServerLocalFileTest {

    SqlServer sqlServer;

    @Before
    public void setUp() {
        System.out.println("===== SET UP =====");
        sqlServer = SqlManagerFactory.getServerLocalFileSqlManager();
        try {
            // Create a database
            sqlServer.createDatabase("DB1");

            // Create a table
            sqlServer.executeStatement(SqlQueryFactory
                    .createTable("DB1", "table1", Arrays.<ColumnMetadata<?>>asList(
                            MetadataFactory.integerBuilder("column1").notNull()
                                    .primaryKey().build(),
                            MetadataFactory.integerBuilder("column2").defaultValue(15)
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
                            Arrays.<ColumnMetadata<?>>asList(
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

    protected ResultSet getTableData(String dbName, String tableName) throws SqlException {
        return sqlServer.getQueryResult(
                SqlQueryFactory.select(TableRefFactory.dbTable(dbName, tableName)));
    }

    static void checkRowExists(ResultSet
                                 resultSet, Map<String, Object> values) {
        ResultRow resultRow = null;
        try {
            for (ResultRow row : resultSet.getRows()) {
                boolean rowMatch = true;
                for (Map.Entry<String, Object> entry : values.entrySet()) {
                    Object value = row.getObject(entry.getKey());

                    if (entry.getValue() == null) {
                        if (value != null) {
                            rowMatch = false;
                            break;
                        } else {
                            continue;
                        }
                    }

                    if (!entry.getValue().equals(value)) {
                        rowMatch = false;
                        break;
                    }
                }
                if (rowMatch) {
                    resultRow = row;
                    break;
                }
            }
            assertNotNull("Row not found!", resultRow);

        } catch (SqlException e) {
            System.out.println(e.getMessage());
            fail();
        }
    }
}
