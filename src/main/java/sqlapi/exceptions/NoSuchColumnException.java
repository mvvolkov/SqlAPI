package sqlapi.exceptions;

import org.jetbrains.annotations.NotNull;

public final class NoSuchColumnException extends SqlException {

    @NotNull
    private final String columnName;

    public NoSuchColumnException(@NotNull String columnName) {
        this.columnName = columnName;
    }

    @Override
    public String getMessage() {
        return "Column not found: " + columnName;
    }

    @NotNull
    public String getColumnName() {
        return columnName;
    }
}
