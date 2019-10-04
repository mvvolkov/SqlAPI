import api.*;

import java.util.Arrays;

public class TestTrivialImpl {

    public static void main(String[] args) {

        SqlManager sqlManager = SqlManagerFactory.getSqlManager();

        Database db1 = sqlManager.createDatabase("DB1");

        db1.createTable(new SqlTableDescription("table1",
                Arrays.asList(SqlColumnDescription.newInteger("column1", true, true),
                        SqlColumnDescription.newInteger("column2", true, true),
                        SqlColumnDescription.newVarchar("column3", true, true, 20))));

        db1.getTableOrNull("table1").insert(new InsertData(Arrays.asList("column1", "column2", "column3"),
                Arrays.asList(10, 15, "test")));
    }
}
