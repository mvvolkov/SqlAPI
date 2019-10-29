package sqlapi.exceptions;

import org.jetbrains.annotations.NotNull;

public final class NoSuchDatabaseException extends SqlException {

    @NotNull
    private final String databaseName;

    public NoSuchDatabaseException(@NotNull String databaseName) {
        this.databaseName = databaseName;
    }

    @Override
    public String getMessage() {
        return "The database with the name " + databaseName + " not found.";
    }

    @NotNull
    public String getDatabaseName() {
        return databaseName;
    }
}
