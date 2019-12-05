package clientImpl.queries;

import org.jetbrains.annotations.NotNull;
import sqlapi.queries.TableActionQuery;

import java.util.ArrayDeque;

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

    protected ArrayDeque<Object> getParametersStack(Object... values) {
        ArrayDeque<Object> parameters = new ArrayDeque<Object>();
        for (int i = values.length - 1; i >= 0; i--) {
            parameters.push(values[i]);
        }
        return parameters;
    }

}
