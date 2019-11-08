package clientDefaultImpl;

import api.SqlStatement;

public abstract class AbstractSqlStatement implements SqlStatement {

    protected final String databaseName;
    protected final String tableName;

    protected AbstractSqlStatement(String databaseName, String tableName) {
        this.databaseName = databaseName;
        this.tableName = tableName;
    }

    @Override
    public String getDatabaseName() {
        return databaseName;
    }

    @Override
    public String getTableName() {
        return tableName;
    }
}