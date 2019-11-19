package sqlapi.exceptions;

import org.jetbrains.annotations.NotNull;

public final class SchemaAlreadyExistsException extends SqlException {

    @NotNull
    private final String databaseName;

    @NotNull
    private final String schemaName;

    public SchemaAlreadyExistsException(@NotNull String databaseName,
                                        @NotNull String schemaName) {
        this.databaseName = databaseName;
        this.schemaName = schemaName;
    }

    @Override
    public String getMessage() {
        return "Schema already exists: " +
                databaseName + "." + schemaName;
    }

    @NotNull
    public String getDatabaseName() {
        return databaseName;
    }

    @NotNull
    public String getSchemaName() {
        return schemaName;
    }

}
