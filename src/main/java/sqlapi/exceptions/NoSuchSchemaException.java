package sqlapi.exceptions;

import org.jetbrains.annotations.NotNull;

public final class NoSuchSchemaException extends SqlException {

    @NotNull
    private final String databaseName;

    @NotNull
    private final String schemaName;

    public NoSuchSchemaException(String databaseName,
                                 @NotNull String schemaName) {
        this.databaseName = databaseName;
        this.schemaName = schemaName;
    }

    @Override
    public String getMessage() {
        return "The schema with the name " + schemaName + " not found.";
    }

    @NotNull
    public String getSchemaName() {
        return schemaName;
    }

    public String getDatabaseName() {
        return databaseName;
    }
}
