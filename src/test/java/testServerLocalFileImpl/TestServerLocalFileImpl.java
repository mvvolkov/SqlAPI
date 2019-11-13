package testServerLocalFileImpl;

import api.selectResult.ResultRow;
import api.selectResult.ResultSet;
import api.SqlServer;
import api.exceptions.NoSuchColumnException;
import api.exceptions.SqlException;
import api.metadata.ColumnMetadata;
import clientImpl.columnExpr.ColumnExprFactory;
import clientImpl.metadata.MetadataFactory;
import clientImpl.predicates.PredicateFactory;
import clientImpl.queries.SqlQueryFactory;
import clientImpl.tableRef.TableRefFactory;
import sqlFactory.SqlManagerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TestServerLocalFileImpl {
    public static void main(String[] args) {

        SqlServer sqlServer = SqlManagerFactory.getServerLocalFileSqlManager();


        try {
            sqlServer.createDatabase("DB1");

            sqlServer.executeStatement(
                    SqlQueryFactory.createTable("DB1", "table1",
                            Arrays.<ColumnMetadata<?>>asList(
                                    MetadataFactory.integerBuilder("column1")
                                            .notNull().primaryKey().build(),
                                    MetadataFactory.integerBuilder("column2")
                                            .defaultValue(15)
                                            .build(),
                                    MetadataFactory.varcharBuilder("column3", 20)
                                            .notNull()
                                            .build()
                            )));

            sqlServer.executeStatement(SqlQueryFactory.insert("DB1", "table1",
                    Arrays.asList(10, 20, "test1")
            ));
            sqlServer.executeStatement(SqlQueryFactory.insert("DB1", "table1",
                    Arrays.asList(11, 20, "test1")));
            sqlServer.executeStatement(SqlQueryFactory.insert("DB1", "table1",
                    Arrays.asList(12, 20, "test1")));
            sqlServer.executeStatement(SqlQueryFactory.insert("DB1", "table1",
                    Arrays.asList(13, 20, "test2")));
            sqlServer.executeStatement(SqlQueryFactory.insert("DB1", "table1",
                    Arrays.asList(15, 21, "test1")));
            printTable(sqlServer, "DB1", "table1");

            sqlServer.executeStatement(
                    SqlQueryFactory.createTable("DB1", "table2",
                            Arrays.<ColumnMetadata<?>>asList(
                                    MetadataFactory.integerBuilder("column4")
                                            .notNull().primaryKey().build(),
                                    MetadataFactory.varcharBuilder("column5", 15)
                                            .build()
                            )));

            sqlServer.executeStatement(SqlQueryFactory.insert("DB1", "table2",
                    Arrays.asList(22, "test2")));
            sqlServer.executeStatement(SqlQueryFactory.insert("DB1", "table2",
                    Arrays.asList(23, "test2")));
            sqlServer.executeStatement(SqlQueryFactory.insert("DB1", "table2",
                    Arrays.asList(25, "test22")));
            printTable(sqlServer, "DB1", "table2");


            ResultSet resultSet = sqlServer.select(SqlQueryFactory.select(Arrays.asList(
                    TableRefFactory.dbTable("DB1", "table2"),
                    TableRefFactory.dbTable("DB1", "table1")),
                    Arrays.asList(
                            ColumnExprFactory.sum(ColumnExprFactory.sum(
                                    ColumnExprFactory
                                            .columnRef("DB1", "table1", "column2"),
                                    ColumnExprFactory
                                            .columnRef("DB1", "table1", "column1")),
                                    ColumnExprFactory.value(35),
                                    "column1 + column2 + 35"),
                            ColumnExprFactory.columnRef("DB1", "table1", "column2"),
                            ColumnExprFactory.columnRef("DB1", "table1", "column1"),
                            ColumnExprFactory.columnRef("DB1", "table1", "column3"),
                            ColumnExprFactory.columnRef("DB1", "table2", "column4"),
                            ColumnExprFactory.columnRef("DB1", "table2", "column5")
                    ),
                    PredicateFactory.equals(ColumnExprFactory.columnRef("DB1", "table2",
                            "column5"),
                            ColumnExprFactory.columnRef("DB1", "table1", "column3"))
                            .and(PredicateFactory.equals(ColumnExprFactory
                                            .columnRef("DB1", "table2", "column4"),
                                    ColumnExprFactory.integer(23)))));

            ResultSet resultSet2 = sqlServer.select(SqlQueryFactory
                    .select(Collections.singletonList(TableRefFactory
                                    .innerJoin(TableRefFactory.dbTable("DB1", "table2"),
                                            TableRefFactory.dbTable("DB1", "table1"),
                                            PredicateFactory.equals(ColumnExprFactory
                                                            .columnRef("DB1", "table2", "column5"),
                                                    ColumnExprFactory.columnRef("DB1", "table1",
                                                            "column3"))
                                                    .and(PredicateFactory.equals(ColumnExprFactory
                                                                    .columnRef("DB1", "table2",
                                                                            "column4"),
                                                            ColumnExprFactory
                                                                    .sum(ColumnExprFactory
                                                                                    .columnRef("DB1",
                                                                                            "table1",
                                                                                            "column2"),
                                                                            ColumnExprFactory
                                                                                    .integer(
                                                                                            3)))))),
                            Collections.EMPTY_LIST,
                            PredicateFactory.empty()));
            printResultSet(resultSet2);

            printResultSet(
                    sqlServer.select(SqlQueryFactory.select(TableRefFactory.dbTable(
                            "DB1", "table1"),
                            PredicateFactory.in(ColumnExprFactory.columnRef("DB1",
                                    "table1", "column1"),
                                    Arrays.asList(ColumnExprFactory.integer(15),
                                            ColumnExprFactory.integer(12),
                                            ColumnExprFactory.integer(13))))));


            sqlServer.executeStatement(SqlQueryFactory.insert("DB1", "table1",
                    Arrays.asList(101, null, "test101")
            ));
            printTable(sqlServer, "DB1", "table1");

            printResultSet(
                    sqlServer.select(SqlQueryFactory.select(TableRefFactory.dbTable(
                            "DB1", "table1"),
                            PredicateFactory.isNull(ColumnExprFactory.columnRef("DB1",
                                    "table1", "column2"
                            )))));

            printResultSet(
                    sqlServer.select(SqlQueryFactory.select(TableRefFactory.dbTable(
                            "DB1", "table1"),
                            PredicateFactory.isNotNull(ColumnExprFactory.columnRef("DB1",
                                    "table1", "column2"
                            )).and(PredicateFactory.equals(ColumnExprFactory.columnRef(
                                    "DB1", "table1", "column2"),
                                    ColumnExprFactory.integer(20))))));


        } catch (SqlException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void printTable(SqlServer sqlServer, String dbName, String tableName)
            throws SqlException {

        System.out.println("");
        ResultSet resultSet = sqlServer.select(SqlQueryFactory
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
