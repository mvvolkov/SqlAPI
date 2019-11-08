package testSimpleFileImpl;

import api.ResultRow;
import api.ResultSet;
import api.SqlClient;
import api.SqlServer;
import api.exceptions.NoSuchColumnException;
import api.exceptions.SqlException;
import clientDefaultImpl.SelectExpressionImpl;
import clientDefaultImpl.SqlClientImpl;
import sqlFactory.SqlManagerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TestSimpleFileImpl {
    public static void main(String[] args) {

        SqlServer sqlServer = SqlManagerFactory.getSimpleFileSqlManager();
        SqlClient sqlClient = new SqlClientImpl();

        try {
            sqlServer.createDatabase("DB1");

            sqlServer.executeStatement(sqlClient.newCreateTableStatement("DB1",
                    "table1", Arrays.asList(
                            sqlClient.getIntegerColumnMetadataBuilder("column1").notNull().primaryKey().build(),
                            sqlClient.getIntegerColumnMetadataBuilder("column2").build(),
                            sqlClient.getVarcharColumnMetadataBuilder("column3", 20).notNull().build())));

            sqlServer.executeStatement(sqlClient.newInsertStatement("DB1", "table1",
                    Arrays.asList(10, 20, "test1")));
            sqlServer.executeStatement(sqlClient.newInsertStatement("DB1", "table1",
                    Arrays.asList(11, 20, "test1")));

            sqlServer.executeStatement(sqlClient.newInsertStatement("DB1", "table1",
                    Arrays.asList(12, 20, "test1")));
            sqlServer.executeStatement(sqlClient.newInsertStatement("DB1", "table1",
                    Arrays.asList(13, 20, "test2")));
            sqlServer.executeStatement(sqlClient.newInsertStatement("DB1", "table1",
                    Arrays.asList(15, 21, "test1")));


            sqlServer.executeStatement(sqlClient.newCreateTableStatement("DB1",
                    "table2", Arrays.asList(
                            sqlClient.getIntegerColumnMetadataBuilder("column4").notNull().primaryKey().build(),
                            sqlClient.getVarcharColumnMetadataBuilder("column5", 15).build()
                    )));

            sqlServer.executeStatement(sqlClient.newInsertStatement("DB1", "table2",
                    Arrays.asList(22, "test2")));
            sqlServer.executeStatement(sqlClient.newInsertStatement("DB1", "table2",
                    Arrays.asList(23, "test2")));
            sqlServer.executeStatement(sqlClient.newInsertStatement("DB1", "table2",
                    Arrays.asList(25, "test22")));


            ResultSet resultSet = sqlServer.select(
                    sqlClient.getSelectionExpressionBuilder(sqlClient.baseTableRef("table2", "DB1"))
                            .addTableReference(sqlClient.baseTableRef("table1", "DB1"))
                            .addPredicate(sqlClient.getPredicateEquals(sqlClient.createColumnReference("column5", "table2"),
                                    sqlClient.createColumnReference("column3", "table1")))
                            .addPredicate(sqlClient.getPredicateEquals(sqlClient.createColumnReference("column4", "table2"), 23))
//                   .addPredicateWithOr(SelectionPredicate.equals(new ColumnReference("column3", "table1"), "test2"))
                            .build());
            printResultSet(resultSet);

            ResultSet resultSet2 = sqlServer.select(SelectExpressionImpl.builder(
                    sqlClient.innerJoin(sqlClient.baseTableRef("table2", "DB1"),
                            sqlClient.baseTableRef("table1", "DB1"),
                            sqlClient.getPredicateEquals(sqlClient.createColumnReference("column5", "table2"),
                                    sqlClient.createColumnReference("column3", "table1"))
                                    .and(sqlClient.getPredicateEquals(sqlClient.createColumnReference("column4", "table2"), 23))))
                    .build());
            printResultSet(resultSet2);


        } catch (SqlException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void printResultSet(ResultSet resultSet) {
        StringBuilder sb = new StringBuilder();


        String tableString = resultSet.getColumns().stream()
                .collect(Collectors.joining(", "));

        sb.append("Select result :\n");
        sb.append(tableString);
        for (ResultRow row : resultSet.getRows()) {
            List<String> values = new ArrayList<>();
            for (String columnName : resultSet.getColumns()) {
                String value = null;
                try {
                    value = row.getObject(columnName).toString();
                } catch (NoSuchColumnException e) {
                    e.printStackTrace();
                }
                values.add(value);
            }
            String rowString = values.stream().collect(Collectors.joining(", "));
            sb.append("\n" + rowString);
        }
        System.out.println(sb);
    }
}
