package sqlapi;

import org.jetbrains.annotations.NotNull;

public final class BaseTableReference extends TableReference {

    @NotNull
    private final String tableName;

    @NotNull
    private final String dbName;

    public static TableReference newTableReference(@NotNull String tableName, @NotNull String dbName) {
        return new BaseTableReference(tableName, dbName);
    }

    public static TableReference newTableReference(@NotNull String tableName, @NotNull String dbName, String alias) {
        return new BaseTableReference(tableName, dbName, alias);
    }

    public BaseTableReference(@NotNull String tableName, @NotNull String dbName) {
        super(tableName);
        this.tableName = tableName;
        this.dbName = dbName;
    }

    public BaseTableReference(String tableName, String dbName, String alias) {
        super(alias);
        this.tableName = tableName;
        this.dbName = dbName;
    }


    @NotNull
    public String getTableName() {
        return tableName;
    }

    @NotNull
    public String getDbName() {
        return dbName;
    }
}
