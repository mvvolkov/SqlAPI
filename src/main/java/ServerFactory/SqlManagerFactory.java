package ServerFactory;

import serverLocalFileImpl.SqlServerImpl;
import serverLoggerImpl.SqlServerLoggerImpl;
import sqlapi.server.SqlServer;

public class SqlManagerFactory {

    public static SqlServer getServerLoggerSqlManager() {
        return new SqlServerLoggerImpl();
    }

    public static SqlServer getServerLocalFileSqlManager() {
        return new SqlServerImpl();
    }
}
