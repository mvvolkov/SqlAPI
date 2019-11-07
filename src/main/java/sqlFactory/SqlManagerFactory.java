package sqlFactory;

import serverFileImpl.SqlServerImpl;
import serverLoggerImpl.SqlServerPrintOutImpl;
import api.SqlServer;

public class SqlManagerFactory {

    public static SqlServer getPrintOutSqlManager() {
        return new SqlServerPrintOutImpl();
    }

    public static SqlServer getSimpleFileSqlManager() {
        return new SqlServerImpl();
    }
}
