package LocalFileDatabase.server;

import sqlapi.server.SqlServer;

public class LocalFileDatabaseServerFactory {

    private LocalFileDatabaseServerFactory() {
    }

    public static SqlServer getServer() {
        return new SqlServerImpl();
    }
}
