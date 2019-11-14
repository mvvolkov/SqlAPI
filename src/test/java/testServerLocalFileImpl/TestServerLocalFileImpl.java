package testServerLocalFileImpl;

import api.SqlServer;
import api.exceptions.NoSuchColumnException;
import api.exceptions.SqlException;
import api.metadata.ColumnMetadata;
import api.selectResult.ResultRow;
import api.selectResult.ResultSet;
import clientImpl.columnExpr.ColumnExprFactory;
import clientImpl.metadata.MetadataFactory;
import clientImpl.predicates.PredicateFactory;
import clientImpl.queries.SqlQueryFactory;
import clientImpl.tableRef.TableRefFactory;
import sqlFactory.SqlManagerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TestServerLocalFileImpl {
    public static void main(String[] args) {

        SqlServer sqlServer = SqlManagerFactory.getServerLocalFileSqlManager();


        try {

            sqlServer.executeStatement(SqlQueryFactory
                    .createTable("table1", Arrays.<ColumnMetadata<?>>asList(
                            MetadataFactory.integerBuilder("column1").notNull()
                                    .primaryKey().build(),
                            MetadataFactory.integerBuilder("column2").defaultValue(15)
                                    .build(),
                            MetadataFactory.varcharBuilder("column3", 20)
                                    .notNull()
                                    .build()
                    )));

            sqlServer.executeStatement(SqlQueryFactory.insert("table1",
                    Arrays.asList(10, 30, "test1")
            ));
            sqlServer.executeStatement(SqlQueryFactory.insert("table1",
                    Arrays.asList(11, 31, "test1")));
            sqlServer.executeStatement(SqlQueryFactory.insert("table1",
                    Arrays.asList(12, 32, "test1")));
            sqlServer.executeStatement(SqlQueryFactory.insert("table1",
                    Arrays.asList(13, 33, "test2")));
            sqlServer.executeStatement(SqlQueryFactory.insert("table1",
                    Arrays.asList(15, 34, "test1")));
            printTable(sqlServer, "DB1", "table1");

            sqlServer.executeStatement(
                    SqlQueryFactory.createTable("table2",
                            Arrays.<ColumnMetadata<?>>asList(
                                    MetadataFactory.integerBuilder("column4")
                                            .notNull().primaryKey().build(),
                                    MetadataFactory.varcharBuilder("column5", 15)
                                            .build()
                            )));

            sqlServer.executeStatement(
                    SqlQueryFactory.insert("table2", Arrays.asList(22, "test2")));
            sqlServer.executeStatement(
                    SqlQueryFactory.insert("table2", Arrays.asList(23, "test2")));
            sqlServer.executeStatement(
                    SqlQueryFactory.insert("table2", Arrays.asList(25, "test22")));
            printTable(sqlServer, "DB1", "table2");


            printResultSet(sqlServer.getQueryResult(SqlQueryFactory.select(Arrays.asList(
                    TableRefFactory.dbTable("DB1", "table2"),
                    TableRefFactory.dbTable("DB1", "table1")),
                    Arrays.asList(
                            ColumnExprFactory.sum(ColumnExprFactory.sum(
                                    ColumnExprFactory.columnRef("column2"),
                                    ColumnExprFactory.columnRef("column1")),
                                    ColumnExprFactory.value(35),
                                    "column1 + column2 + 35"),
                            ColumnExprFactory.columnRef("column2"),
                            ColumnExprFactory.columnRef("column1"),
                            ColumnExprFactory.columnRef("column3"),
                            ColumnExprFactory.columnRef("column4"),
                            ColumnExprFactory.columnRef("column5")
                    ),
                    PredicateFactory.equals(ColumnExprFactory.columnRef("column5"),
                            ColumnExprFactory.columnRef("column3")).and(PredicateFactory
                            .equals(ColumnExprFactory.columnRef("column4"),
                                    ColumnExprFactory.integer(23))))));


            printResultSet(sqlServer.getQueryResult(SqlQueryFactory.select(TableRefFactory
                    .innerJoin(TableRefFactory.dbTable("DB1", "table2"),
                            TableRefFactory.dbTable("DB1", "table1"),
                            PredicateFactory
                                    .equals(ColumnExprFactory.columnRef("column5"),
                                            ColumnExprFactory.columnRef("column3"))
                                    .and(PredicateFactory.equals(ColumnExprFactory
                                                    .columnRef("column2"),
                                            ColumnExprFactory.sum(ColumnExprFactory
                                                            .columnRef("column4"),
                                                    ColumnExprFactory.integer(10))))))));


            printResultSet(
                    sqlServer.getQueryResult(
                            SqlQueryFactory
                                    .select(TableRefFactory.dbTable("DB1", "table1"),
                                            PredicateFactory.in(ColumnExprFactory
                                                            .columnRef("column1"),
                                                    Arrays.asList(
                                                            ColumnExprFactory
                                                                    .integer(15),
                                                            ColumnExprFactory
                                                                    .integer(12),
                                                            ColumnExprFactory
                                                                    .integer(13)
                                                    )
                                            ))));


            sqlServer.executeStatement(
                    SqlQueryFactory.insert("table1", Arrays.asList(101, null, "test101")
                    ));
            printTable(sqlServer, "DB1", "table1");

            printResultSet(sqlServer.getQueryResult(SqlQueryFactory
                    .select(TableRefFactory.dbTable("DB1", "table1"), PredicateFactory
                            .isNull(ColumnExprFactory.columnRef("column2")))));

            printResultSet(
                    sqlServer.getQueryResult(
                            SqlQueryFactory.select(TableRefFactory.dbTable(
                                    "DB1", "table1"),
                                    PredicateFactory
                                            .isNotNull(
                                                    ColumnExprFactory.columnRef("column2"
                                                    )).and(PredicateFactory
                                            .equals(ColumnExprFactory.columnRef(
                                                    "column2"),
                                                    ColumnExprFactory.integer(30))))));

            sqlServer.executeStatement(SqlQueryFactory.insert("table2",
                    SqlQueryFactory.select(TableRefFactory.dbTable("DB1", "table1"),
                            Arrays.asList(ColumnExprFactory.columnRef("column2"),
                                    ColumnExprFactory.columnRef("column3")),
                            PredicateFactory
                                    .isNotNull(ColumnExprFactory.columnRef("column2"
                                    )))));

            printTable(sqlServer, "DB1", "table2");


        } catch (SqlException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void printTable(SqlServer sqlServer, String dbName, String tableName)
            throws SqlException {

        System.out.println("");
        ResultSet resultSet = sqlServer.getQueryResult(SqlQueryFactory
                .select(TableRefFactory.dbTable(dbName, tableName)));
        printResultSet(resultSet);
    }

    private static void printResultSet(ResultSet resultSet)
            throws NoSuchColumnException {
        StringBuilder sb = new StringBuilder();


        String columnsHeaders = resultSet.getColumns().stream()
                .collect(Collectors.joining(", "));

        sb.append(columnsHeaders);
        for (ResultRow row : resultSet.getRows()) {
            List<String> values = new ArrayList<>();
            for (String columnName : resultSet.getColumns()) {
                values.add(String.valueOf(row.getObject(columnName)));
            }
            String rowString =
                    values.stream().collect(Collectors.joining(", "));
            sb.append("\n" + rowString);
        }
        System.out.println(sb);
        System.out.println("");
    }
}
