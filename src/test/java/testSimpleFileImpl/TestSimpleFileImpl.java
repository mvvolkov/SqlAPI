package testSimpleFileImpl;

import api.ResultRow;
import api.ResultSet;
import api.SqlServer;
import api.exceptions.NoSuchColumnException;
import api.exceptions.SqlException;
import api.metadata.ColumnMetadata;
import clientImpl.columnExpr.ColumnExprFactory;
import clientImpl.metadata.ColumnMetadataFactory;
import clientImpl.predicates.PredicateFactory;
import clientImpl.queries.SqlQueryFactory;
import clientImpl.selectedItems.SelectedItemFactory;
import clientImpl.tableRef.TableRefFactory;
import sqlFactory.SqlManagerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TestSimpleFileImpl {
    public static void main(String[] args) {

        SqlServer sqlServer = SqlManagerFactory.getSimpleFileSqlManager();


        try {
            sqlServer.createDatabase("DB1");

            sqlServer.executeStatement(
                    SqlQueryFactory.createTable("DB1", "table1",
                            Arrays.<ColumnMetadata<?>>asList(
                                    ColumnMetadataFactory.integerBuilder("column1")
                                            .notNull().primaryKey().build(),
                                    ColumnMetadataFactory.integerBuilder("column2")
                                            .build(),
                                    ColumnMetadataFactory.varcharBuilder("column3", 20)
                                            .notNull()
                                            .build()
                            )));

            sqlServer.executeStatement(
                    SqlQueryFactory.insert("DB1", "table1",
                            Arrays.asList(ColumnExprFactory.integer(10),
                                    ColumnExprFactory.integer(20),
                                    ColumnExprFactory.string("test1"))
                    ));
            sqlServer.executeStatement(
                    SqlQueryFactory.insert("DB1", "table1",
                            Arrays.asList(ColumnExprFactory.integer(11),
                                    ColumnExprFactory.integer(20),
                                    ColumnExprFactory.string("test1"))));
            sqlServer.executeStatement(
                    SqlQueryFactory.insert("DB1", "table1",
                            Arrays.asList(ColumnExprFactory.integer(12),
                                    ColumnExprFactory.integer(20),
                                    ColumnExprFactory.string("test1"))));
            sqlServer.executeStatement(
                    SqlQueryFactory.insert("DB1", "table1",
                            Arrays.asList(ColumnExprFactory.integer(13),
                                    ColumnExprFactory.integer(20),
                                    ColumnExprFactory.string("test2"))));
            sqlServer.executeStatement(
                    SqlQueryFactory.insert("DB1", "table1",
                            Arrays.asList(ColumnExprFactory.integer(15),
                                    ColumnExprFactory.integer(21),
                                    ColumnExprFactory.string("test1"))));


            sqlServer.executeStatement(
                    SqlQueryFactory.createTable("DB1", "table2",
                            Arrays.<ColumnMetadata<?>>asList(
                                    ColumnMetadataFactory.integerBuilder("column4")
                                            .notNull().primaryKey().build(),
                                    ColumnMetadataFactory.varcharBuilder("column5", 15)
                                            .build()
                            )));

            sqlServer.executeStatement(
                    SqlQueryFactory.insert("DB1", "table2",
                            Arrays.asList(ColumnExprFactory.integer(22),
                                    ColumnExprFactory.string("test2"))));
            sqlServer.executeStatement(
                    SqlQueryFactory.insert("DB1", "table2",
                            Arrays.asList(ColumnExprFactory.integer(23),
                                    ColumnExprFactory.string("test2"))));
            sqlServer.executeStatement(
                    SqlQueryFactory.insert("DB1", "table2",
                            Arrays.asList(ColumnExprFactory.integer(25),
                                    ColumnExprFactory.string("test22"))));


            ResultSet resultSet = sqlServer.select(SqlQueryFactory.select(Arrays.asList(
                    TableRefFactory.dbTable("DB1", "table2"),
                    TableRefFactory.dbTable("DB1", "table1")),
                    Arrays.asList(SelectedItemFactory.all()), PredicateFactory
                            .equals(ColumnExprFactory.columnRef("DB1", "table2",
                                    "column5"),
                                    ColumnExprFactory
                                            .columnRef("DB1", "table1", "column3"))
                            .and(PredicateFactory
                                    .equals(ColumnExprFactory
                                                    .columnRef("DB1", "table2", "column4"),
                                            ColumnExprFactory.integer(23)))));
            printResultSet(resultSet);

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
                                                            ColumnExprFactory.integer(23))))),
                            Collections.singletonList(SelectedItemFactory.all()),
                            PredicateFactory.empty()));
            printResultSet(resultSet2);


        } catch (SqlException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void printResultSet(ResultSet resultSet)
            throws NoSuchColumnException {
        StringBuilder sb = new StringBuilder();


        String tableString = resultSet.getColumns().stream()
                .collect(Collectors.joining(", "));

        sb.append("Select result:\n");
        sb.append(tableString);
        for (ResultRow row : resultSet.getRows()) {
            List<String> values = new ArrayList<>();
            for (String columnName : resultSet.getColumns()) {
                values.add(row.getObject(columnName).toString());
            }
            String rowString =
                    values.stream().collect(Collectors.joining(", "));
            sb.append("\n" + rowString);
        }
        System.out.println(sb);
    }
}
