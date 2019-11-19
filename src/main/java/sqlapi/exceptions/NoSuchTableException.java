package sqlapi.exceptions;

import org.jetbrains.annotations.NotNull;

public final class NoSuchTableException extends SqlException {

    @NotNull
    private final String tableName;

    public NoSuchTableException(@NotNull String tableName) {
        this.tableName = tableName;
    }

    @Override
    public String getMessage() {
        return "The table with the name " + tableName + " not found.";
    }

    @NotNull
    public String getTableName() {
        return tableName;
    }
}
