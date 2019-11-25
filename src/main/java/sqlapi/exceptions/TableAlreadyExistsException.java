package sqlapi.exceptions;

import org.jetbrains.annotations.NotNull;

public final class TableAlreadyExistsException extends SqlException {

    @NotNull
    private final String tableName;

    @NotNull
    private final String databaseName;

    public TableAlreadyExistsException(@NotNull String databaseName,
                                       @NotNull String tableName) {
        this.databaseName = databaseName;
        this.tableName = tableName;
    }

    @Override
    public String getMessage() {
        return "Can not create a new table. The table " + databaseName
                + "." + tableName + " already exists.";
    }

    @NotNull
    public String getTableName() {
        return tableName;
    }

    @NotNull
    public String getDatabaseName() {
        return databaseName;
    }
}
