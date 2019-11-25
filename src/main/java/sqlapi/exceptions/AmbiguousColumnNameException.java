package sqlapi.exceptions;

import org.jetbrains.annotations.NotNull;

public class AmbiguousColumnNameException extends SqlException {

    @NotNull
    private final String columnName;


    public AmbiguousColumnNameException(@NotNull String columnName) {
        this.columnName = columnName;
    }

    @NotNull
    public String getColumnName() {
        return columnName;
    }

    @Override
    public String getMessage() {
        return "Ambiguous column name: " + columnName;
    }
}
