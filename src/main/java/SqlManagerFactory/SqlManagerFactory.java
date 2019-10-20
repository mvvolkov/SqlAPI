package SqlManagerFactory;

import SimplePrintOutImpl.SqlManagerImpl;
import sqlapi.SqlManager;

public class SqlManagerFactory {

    public static SqlManager getPrintOutSqlManager() {
        return new SqlManagerImpl();
    }
}
