package serverLocalFileImpl;

import api.columnExpr.ColumnRef;

public class ColumnRefImpl implements ColumnRef {

    private final String databaseName;
    private final String tableName;
    private final String columnName;

    public ColumnRefImpl(String databaseName, String tableName, String columnName) {
        this.databaseName = databaseName;
        this.tableName = tableName;
        this.columnName = columnName;
    }


    @Override
    public String getColumnName() {
        return columnName;
    }

    @Override
    public String getTableName() {
        return tableName;
    }

    @Override
    public String getDatabaseName() {
        return databaseName;
    }

    @Override
    public String toString() {
        return databaseName + "." + tableName + "." + columnName;
    }
}
