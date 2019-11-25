package serverLocalFileImpl;

public class SqlLocalFileServerFactory {

    private SqlLocalFileServerFactory() {
    }

    public static SqlServerLocalFile getServer() {
        return new SqlServerImpl();
    }
}
