package clientImpl.queries;

import org.jetbrains.annotations.NotNull;
import sqlapi.metadata.TableMetadata;
import sqlapi.queries.ReadDatabaseQuery;

import java.util.Collection;

final class ReadDatabaseQueryImpl implements ReadDatabaseQuery {

    @NotNull private final String databaseName;

    @NotNull private final Collection<TableMetadata> tables;

    ReadDatabaseQueryImpl(
            @NotNull String databaseName,
            @NotNull Collection<TableMetadata> tables) {
        this.databaseName = databaseName;
        this.tables = tables;
    }

    @Override public @NotNull String getDatabaseName() {
        return databaseName;
    }

    @Override public @NotNull Collection<TableMetadata> getTables() {
        return tables;
    }
}
