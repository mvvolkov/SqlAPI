package clientImpl.queries;

import org.jetbrains.annotations.NotNull;
import sqlapi.queries.CreateDatabaseQuery;

final class CreateDatabaseQueryImpl implements CreateDatabaseQuery {

    @NotNull private final String databaseName;

    CreateDatabaseQueryImpl(
            @NotNull String databaseName) {
        this.databaseName = databaseName;
    }

    @Override public @NotNull String getDatabaseName() {
        return databaseName;
    }
}
