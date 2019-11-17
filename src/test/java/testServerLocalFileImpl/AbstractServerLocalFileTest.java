package testServerLocalFileImpl;

import api.connect.SqlServer;
import api.exceptions.SqlException;
import api.metadata.ColumnMetadata;
import api.selectResult.ResultRow;
import api.selectResult.ResultSet;
import clientImpl.metadata.MetadataFactory;
import clientImpl.queries.SqlQueryFactory;
import org.junit.Before;
import sqlFactory.SqlManagerFactory;

import java.util.Arrays;
import java.util.Map;

import static org.junit.Assert.*;

public class AbstractServerLocalFileTest {

    protected SqlServer sqlServer;

    @Before
    public void setUp() {
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
                                    .build()
                    )));

            // Fill table1
            sqlServer.executeStatement(SqlQueryFactory.insert("DB1", "table1",
                    Arrays.asList(10, 30, "test1")));
            sqlServer.executeStatement(SqlQueryFactory.insert("DB1", "table1",
                    Arrays.asList(11, 31, "test2")));
            sqlServer.executeStatement(SqlQueryFactory.insert("DB1", "table1",
                    Arrays.asList(12, 32, "test3")));
            sqlServer.executeStatement(SqlQueryFactory.insert("DB1", "table1",
                    Arrays.asList(13, 33, "test2")));
            sqlServer.executeStatement(SqlQueryFactory.insert("DB1", "table1",
                    Arrays.asList(15, 34, "test1")));
            sqlServer.executeStatement(SqlQueryFactory.insert("DB1", "table1",
                    Arrays.asList(16, null, "test1")));

            // Create a table
            sqlServer.executeStatement(
                    SqlQueryFactory.createTable("DB1", "table2",
                            Arrays.<ColumnMetadata<?>>asList(
                                    MetadataFactory.integerBuilder("column4")
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

        } catch (SqlException e) {
            System.out.println(e.getMessage());
        }
    }

    protected static void checkRow(ResultSet
                                           resultSet, Map<String, Object> keyValues,
                                   Map<String, Object> otherValues) {
        ResultRow resultRow = null;
        try {
            for (ResultRow row : resultSet.getRows()) {
                boolean rowMatch = true;
                for (Map.Entry<String, Object> entry : keyValues.entrySet()) {
                    if (!row.getObject(entry.getKey()).equals(entry.getValue())) {
                        rowMatch = false;
                        break;
                    }
                }
                if (rowMatch) {
                    resultRow = row;
                    break;
                }
            }
            assertNotNull(resultRow);

            for (Map.Entry<String, Object> entry : otherValues.entrySet()) {
                assertEquals(entry.getValue(), resultRow.getObject(entry.getKey()));
            }
        } catch (SqlException e) {
            System.out.println(e.getMessage());
            fail();
        }
    }
}
