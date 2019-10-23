package SqlManagerFactory;

import SimplePrintOutImpl.SqlManagerImpl;
import sqlapi.SqlManager;

public class SqlManagerFactory {

    public static SqlManager getPrintOutSqlManager() {
        return new SqlManagerImpl();
    }

    public static SqlManager getSimpleFileSqlManager() {
        return new SimpleFileImpl.SqlManagerImpl();
    }
}
