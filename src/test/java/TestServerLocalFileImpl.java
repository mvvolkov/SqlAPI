import api.connect.SqlServer;
import api.exceptions.SqlException;
import api.selectResult.ResultRow;
import api.selectResult.ResultSet;
import clientImpl.columnExpr.ColumnExprFactory;
import clientImpl.metadata.MetadataFactory;
import clientImpl.predicates.PredicateFactory;
import clientImpl.queries.SqlQueryFactory;
import clientImpl.tableRef.TableRefFactory;
import sqlFactory.SqlManagerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

public class TestServerLocalFileImpl {


    public static void main(String[] args) {

        System.out.println("");
        SqlServer sqlServer = SqlManagerFactory.getServerLocalFileSqlManager();


        try {

            sqlServer.createDatabase("DB1");

            sqlServer.executeStatement(SqlQueryFactory
                    .createTable("DB1", "table1", Arrays.asList(
                            MetadataFactory.integerBuilder("column1").notNull()
                                    .primaryKey().build(),
                            MetadataFactory.integerBuilder("column2").defaultValue(15)
                                    .build(),
                            MetadataFactory.varcharBuilder("column3", 20)
                                    .notNull()
                                    .build()
                    )));

            sqlServer.executeStatement(SqlQueryFactory.insert("DB1", "table1",
                    Arrays.asList(10, 30, "test1")
            ));
            sqlServer.executeStatement(SqlQueryFactory.insert("DB1", "table1",
                    Arrays.asList(11, 31, "test2")));
            sqlServer.executeStatement(SqlQueryFactory.insert("DB1", "table1",
                    Arrays.asList(12, 32, "test3")));
            sqlServer.executeStatement(SqlQueryFactory.insert("DB1", "table1",
                    Arrays.asList(13, 33, "test2")));
            sqlServer.executeStatement(SqlQueryFactory.insert("DB1", "table1",
                    Arrays.asList(15, 34, "test1")));


            System.out.println("");
            printResultSet(sqlServer.getQueryResult(
                    SqlQueryFactory
                            .select(TableRefFactory.dbTable("DB1", "table1"))));

            sqlServer.executeStatement(
                    SqlQueryFactory.createTable("DB1", "table2",
                            Arrays.asList(
                                    MetadataFactory.integerBuilder("column4")
                                            .notNull().primaryKey().build(),
                                    MetadataFactory.varcharBuilder("column3", 15)
                                            .build()
                            )));

            sqlServer.executeStatement(
                    SqlQueryFactory
                            .insert("DB1", "table2", Arrays.asList(22, "test3")));
            sqlServer.executeStatement(
                    SqlQueryFactory
                            .insert("DB1", "table2", Arrays.asList(23, "test2")));
            sqlServer.executeStatement(
                    SqlQueryFactory
                            .insert("DB1", "table2", Arrays.asList(25, "test4")));
            System.out.println("");
            printResultSet(sqlServer.getQueryResult(
                    SqlQueryFactory
                            .select(TableRefFactory.dbTable("DB1", "table2"))));


            System.out.println("");
            printResultSet(
                    sqlServer.getQueryResult(SqlQueryFactory.select(TableRefFactory
                            .innerJoin(TableRefFactory.dbTable("DB1", "table2"),
                                    TableRefFactory.dbTable("DB1", "table1"),
                                    PredicateFactory
                                            .equals(ColumnExprFactory
                                                            .columnRef("table2", "column3"),
                                                    ColumnExprFactory
                                                            .columnRef("table1",
                                                                    "column3"))
                                            .and(PredicateFactory
                                                    .equals(ColumnExprFactory
                                                                    .columnRef("column2"),
                                                            ColumnExprFactory
                                                                    .sum(ColumnExprFactory
                                                                                    .columnRef(
                                                                                            "column4"),
                                                                            ColumnExprFactory
                                                                                    .value(
                                                                                            10))))))));

            System.out.println("");
            printResultSet(
                    sqlServer.getQueryResult(
                            SqlQueryFactory
                                    .select(TableRefFactory.dbTable("DB1", "table1"),
                                            PredicateFactory.in(ColumnExprFactory
                                                            .columnRef("column1"),
                                                    Arrays.asList(
                                                            ColumnExprFactory
                                                                    .value(15),
                                                            ColumnExprFactory
                                                                    .value(12),
                                                            ColumnExprFactory
                                                                    .value(13)
                                                    )
                                            ))));


            sqlServer.executeStatement(
                    SqlQueryFactory
                            .insert("DB1", "table1",
                                    Arrays.asList(101, null, "test101")
                            ));
            System.out.println("");
            printResultSet(sqlServer.getQueryResult(
                    SqlQueryFactory
                            .select(TableRefFactory.dbTable("DB1", "table1"))));

            System.out.println("");
            printResultSet(sqlServer.getQueryResult(SqlQueryFactory
                    .select(TableRefFactory.dbTable("DB1", "table1"), PredicateFactory
                            .isNull(ColumnExprFactory.columnRef("column2")))));

            System.out.println("");
            printResultSet(
                    sqlServer.getQueryResult(
                            SqlQueryFactory.select(TableRefFactory.dbTable("DB1",
                                    "table1"),
                                    PredicateFactory
                                            .isNotNull(
                                                    ColumnExprFactory
                                                            .columnRef("column2"
                                                            )).and(PredicateFactory
                                            .equals(ColumnExprFactory.columnRef(
                                                    "column2"),
                                                    ColumnExprFactory
                                                            .value(30))))));

            sqlServer.executeStatement(SqlQueryFactory.insert("DB1", "table2",
                    SqlQueryFactory.select(TableRefFactory.dbTable("DB1", "table1"),
                            Arrays.asList(ColumnExprFactory.columnRef("column2"),
                                    ColumnExprFactory.columnRef("column3")),
                            PredicateFactory
                                    .isNotNull(ColumnExprFactory.columnRef("column2"
                                    )))));
            System.out.println("");
            printResultSet(sqlServer.getQueryResult(
                    SqlQueryFactory
                            .select(TableRefFactory.dbTable("DB1", "table2"))));

            System.out.println("");
            printResultSet(sqlServer.getQueryResult(SqlQueryFactory.select(
                    Arrays.asList(
                            TableRefFactory.dbTable("DB1", "table2"),
                            TableRefFactory.tableFromSelect(
                                    SqlQueryFactory
                                            .select(TableRefFactory
                                                            .dbTable("DB1", "table1"),
                                                    Arrays.asList(
                                                            ColumnExprFactory
                                                                    .columnRef(
                                                                            "column2"),
                                                            ColumnExprFactory
                                                                    .columnRef(
                                                                            "column3")
                                                    )), "t1")
                    ),
                    PredicateFactory
                            .equals(ColumnExprFactory.columnRef("table2", "column3"),
                                    ColumnExprFactory.columnRef("t1", "column3"))
                    ))
            );

            printResultSet(sqlServer.getQueryResult(SqlQueryFactory.selectGrouped(
                    TableRefFactory.dbTable("DB1", "table2"),
                    Arrays.asList(ColumnExprFactory.columnRef("column3"),
                            ColumnExprFactory.sum(ColumnExprFactory.countAll(),
                                    ColumnExprFactory.value(1), "C1")),
                    Collections
                            .singletonList(ColumnExprFactory.columnRef("column3")))));

            printResultSet(sqlServer.getQueryResult(SqlQueryFactory.selectGrouped(
                    TableRefFactory.dbTable("DB1", "table2"),
                    Arrays.asList(ColumnExprFactory.columnRef("column3"),
                            ColumnExprFactory.aggregateSum("column4")),
                    Collections
                            .singletonList(ColumnExprFactory.columnRef("column3")))));

            printResultSet(sqlServer.getQueryResult(SqlQueryFactory.selectGrouped(
                    TableRefFactory.dbTable("DB1", "table2"),
                    Arrays.asList(ColumnExprFactory.columnRef("column3"),
                            ColumnExprFactory.max("column4")),
                    Collections
                            .singletonList(ColumnExprFactory.columnRef("column3")))));

            printResultSet(sqlServer.getQueryResult(SqlQueryFactory.selectGrouped(
                    TableRefFactory.dbTable("DB1", "table2"),
                    Arrays.asList(ColumnExprFactory.columnRef("column3"),
                            ColumnExprFactory.min("column4")),
                    Collections
                            .singletonList(ColumnExprFactory.columnRef("column3")))));


        } catch (SqlException e) {
            System.out.println(e.getMessage());
        }
    }


    private static void printResultSet(ResultSet resultSet) {

        StringBuilder sb = new StringBuilder();

        String columnsHeaders = resultSet.getHeaders().stream()
                .collect(Collectors.joining(", "));

        sb.append(columnsHeaders);
        for (ResultRow row : resultSet.getRows()) {
            String rowString =
                    row.getValues().stream().map(o -> String.valueOf(o))
                            .collect(Collectors.joining(
                                    ", "));
            sb.append("\n" + rowString);
        }
        System.out.println(sb);
        System.out.println("");
    }
}
