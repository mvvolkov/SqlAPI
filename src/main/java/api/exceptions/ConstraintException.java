package api.exceptions;

import org.jetbrains.annotations.NotNull;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ConstraintException extends SqlException {


    @NotNull
    private final String databaseName;

    @NotNull
    private final String tableName;

    @NotNull
    private final String columnName;

    @NotNull
    private final String reason;


    public ConstraintException(
            String databaseName, String tableName, String columnName,
            @NotNull String reason) {
        this.databaseName = databaseName;
        this.tableName = tableName;
        this.columnName = columnName;
        this.reason = reason;
    }

    @Override
    public String getMessage() {
        return "Constraint violation for the column "
                + Stream.of(databaseName, tableName, columnName)
                .collect(Collectors.joining(".")) +
                ": " + reason;
    }


    @NotNull
    public String getReason() {
        return reason;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getTableName() {
        return tableName;
    }

    public String getColumnName() {
        return columnName;
    }
}
