package clientImpl.queries;

import org.jetbrains.annotations.NotNull;
import sqlapi.queries.DropTableQuery;

final class DropTableQueryImpl implements DropTableQuery {

    private final @NotNull String databaseName;
    private final @NotNull String tableName;

    DropTableQueryImpl(@NotNull String databaseName, @NotNull String tableName) {
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
        return "DROP TABLE " + this.getDatabaseName() + "." + this.getTableName();
    }
}
