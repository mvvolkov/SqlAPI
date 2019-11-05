package api.exceptions;

import org.jetbrains.annotations.NotNull;

public final class DatabaseAlreadyExistsException extends SqlException {

    @NotNull
    private final String databaseName;

    public DatabaseAlreadyExistsException(@NotNull String databaseName) {
        this.databaseName = databaseName;
    }

    @Override
    public String getMessage() {
        return "Can not create a new database. The database with the name " + databaseName + " already exists.";
    }

    @NotNull
    public String getDatabaseName() {
        return databaseName;
    }
}
