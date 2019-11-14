package api.exceptions;

import org.jetbrains.annotations.NotNull;

public final class NoSuchSchemaException extends SqlException {

    @NotNull
    private final String schemaName;

    public NoSuchSchemaException(@NotNull String schemaName) {
        this.schemaName = schemaName;
    }

    @Override
    public String getMessage() {
        return "The database with the name " + schemaName + " not found.";
    }

    @NotNull
    public String getSchemaName() {
        return schemaName;
    }
}
