package sqlapi.tableReference;

import org.jetbrains.annotations.NotNull;

public final class BaseTableReference extends TableReference {

    @NotNull
    private final String tableName;

    @NotNull
    private final String dbName;



    BaseTableReference(@NotNull String tableName, @NotNull String dbName) {
        super(tableName);
        this.tableName = tableName;
        this.dbName = dbName;
    }

    BaseTableReference(String tableName, String dbName, String alias) {
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
