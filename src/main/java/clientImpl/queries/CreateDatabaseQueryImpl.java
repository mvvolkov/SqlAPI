package clientImpl.queries;

import org.jetbrains.annotations.NotNull;
import sqlapi.queries.CreateDatabaseQuery;

final class CreateDatabaseQueryImpl implements CreateDatabaseQuery {

    @NotNull private final String databaseName;

    private final boolean checkExistence;

    CreateDatabaseQueryImpl(
            @NotNull String databaseName, boolean checkExistence) {
        this.databaseName = databaseName;
        this.checkExistence = checkExistence;
    }

    @Override public @NotNull String getDatabaseName() {
        return databaseName;
    }

    @Override
    public boolean checkExistence() {
        return checkExistence;
    }

    @Override public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE DATABASE ");
        if (checkExistence) {
            sb.append("IF NOT EXISTS ");
        }
        sb.append(databaseName);
        return sb.toString();
    }
}
