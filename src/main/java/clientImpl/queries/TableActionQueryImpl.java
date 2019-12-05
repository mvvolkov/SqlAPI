package clientImpl.queries;

import org.jetbrains.annotations.NotNull;
import sqlapi.queries.TableActionQuery;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

abstract class TableActionQueryImpl implements TableActionQuery {

    private final @NotNull String databaseName;
    private final @NotNull String tableName;

    TableActionQueryImpl(@NotNull String databaseName, @NotNull String tableName) {
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
