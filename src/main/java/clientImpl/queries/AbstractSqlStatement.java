package clientImpl.queries;

import api.queries.SqlStatement;

public abstract class AbstractSqlStatement implements SqlStatement {

    protected final String tableName;

    protected AbstractSqlStatement(String tableName) {
        this.tableName = tableName;
    }


    @Override
    public String getTableName() {
        return tableName;
    }
}
