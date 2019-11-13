package sqlFactory;

import serverLocalFileImpl.SqlServerImpl;
import serverLoggerImpl.SqlServerLoggerImpl;
import api.SqlServer;

public class SqlManagerFactory {

    public static SqlServer getServerLoggerSqlManager() {
        return new SqlServerLoggerImpl();
    }

    public static SqlServer getServerLocalFileSqlManager() {
        return new SqlServerImpl();
    }
}
