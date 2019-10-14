package SqlManagerFactory;

import SimplePrintOutImpl.SqlManagerImpl;
import api.SqlManager;

public class SqlManagerFactory {

    public static SqlManager getPrintOutSqlManager() {
        return new SqlManagerImpl();
    }
}
