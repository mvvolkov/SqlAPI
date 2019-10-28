package SqlManagerFactory;

import SimplePrintOutImpl.SqlManagerPrintOutImpl;
import sqlapi.SqlManager;

public class SqlManagerFactory {

    public static SqlManager getPrintOutSqlManager() {
        return new SqlManagerPrintOutImpl();
    }

    public static SqlManager getSimpleFileSqlManager() {
        return new SimpleFileImpl.SqlManagerImpl();
    }
}
