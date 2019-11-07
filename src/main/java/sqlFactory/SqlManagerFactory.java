package sqlFactory;

import serverFileImpl.SqlServerImpl;
import serverLoggerImpl.SqlServerLoggerImpl;
import api.SqlServer;

public class SqlManagerFactory {

    public static SqlServer getPrintOutSqlManager() {
        return new SqlServerLoggerImpl();
    }

    public static SqlServer getSimpleFileSqlManager() {
        return new SqlServerImpl();
    }
}
