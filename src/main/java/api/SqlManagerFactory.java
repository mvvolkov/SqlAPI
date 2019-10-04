package api;

import trivialImpl.SqlManagerImpl;

public class SqlManagerFactory {

    public static SqlManager getSqlManager(){
        return new SqlManagerImpl();
    }
}
