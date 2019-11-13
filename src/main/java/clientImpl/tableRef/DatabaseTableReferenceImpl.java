package clientImpl.tableRef;

import org.jetbrains.annotations.NotNull;
import api.tables.DatabaseTableReference;

public final class DatabaseTableReferenceImpl implements DatabaseTableReference {

    @NotNull
    private final String databaseName;

    @NotNull
    private final String tableName;


    DatabaseTableReferenceImpl(@NotNull String dbName, @NotNull String tableName) {
        this.tableName = tableName;
        this.databaseName = dbName;
    }


    @NotNull
    @Override
    public String getTableName() {
        return tableName;
    }

    @NotNull
    @Override
    public String getDatabaseName() {
        return databaseName;
    }

}
