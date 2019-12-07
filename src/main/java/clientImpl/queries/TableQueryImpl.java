package clientImpl.queries;

import org.jetbrains.annotations.NotNull;
import sqlapi.queries.TableQuery;

abstract class TableQueryImpl implements TableQuery {

    private final @NotNull String databaseName;
    private final @NotNull String tableName;

    TableQueryImpl(@NotNull String databaseName, @NotNull String tableName) {
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

}
