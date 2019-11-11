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
    private final String errorMessage;


    public ConstraintException(
            String databaseName, String tableName, String columnName,
            @NotNull String errorMessage) {
        this.databaseName = databaseName;
        this.tableName = tableName;
        this.columnName = columnName;
        this.errorMessage = errorMessage;
    }

    @Override
    public String getMessage() {
        return "Constraint violation for the column "
                + Stream.of(databaseName, tableName, columnName)
                .collect(Collectors.joining(".")) +
                ": " + errorMessage;
    }


    @NotNull
    public String getErrorMessage() {
        return errorMessage;
    }
}
