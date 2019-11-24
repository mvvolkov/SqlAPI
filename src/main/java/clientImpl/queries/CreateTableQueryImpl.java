package clientImpl.queries;

import org.jetbrains.annotations.NotNull;
import sqlapi.metadata.TableMetadata;
import sqlapi.queries.CreateTableQuery;

final class CreateTableQueryImpl
        implements CreateTableQuery {

    @NotNull
    protected final String databaseName;

    @NotNull
    private final TableMetadata tableMetadata;

    CreateTableQueryImpl(@NotNull String databaseName,
                         @NotNull TableMetadata tableMetadata) {
        this.databaseName = databaseName;
        this.tableMetadata = tableMetadata;
    }

    @Override public @NotNull String getDatabaseName() {
        return databaseName;
    }

    @Override public @NotNull TableMetadata getTableMetadata() {
        return tableMetadata;
    }
}
