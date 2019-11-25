package clientImpl.tableRef;

import sqlapi.tables.DatabaseTableReference;
import org.jetbrains.annotations.NotNull;

final class DatabaseTableReferenceImpl implements DatabaseTableReference {

    @NotNull
    private final String databaseName;

    @NotNull
    private final String tableName;


    DatabaseTableReferenceImpl(@NotNull String databaseName, @NotNull String tableName) {
        this.databaseName = databaseName;
        this.tableName = tableName;
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

    @Override
    public String toString() {
        return databaseName + "." + tableName;
    }
}
