package api.exceptions;

import org.jetbrains.annotations.NotNull;

public final class TableAlreadyExistsException extends SqlException {

    @NotNull
    private final String tableName;

    public TableAlreadyExistsException(@NotNull String tableName) {
        this.tableName = tableName;
    }

    @Override
    public String getMessage() {
        return "Can not create a new table. The table with the name " + tableName + " already exists.";
    }

    public String getTableName() {
        return tableName;
    }
}
