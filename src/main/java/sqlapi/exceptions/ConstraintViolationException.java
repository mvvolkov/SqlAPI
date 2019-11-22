package sqlapi.exceptions;

import org.jetbrains.annotations.NotNull;
import sqlapi.metadata.ColumnConstraintType;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ConstraintViolationException extends SqlException {


    @NotNull
    private final String databaseName;

    @NotNull
    private final String tableName;

    @NotNull
    private final String columnName;

    @NotNull
    private final ColumnConstraintType constraintType;


    public ConstraintViolationException(
            String databaseName, String tableName, String columnName,
            @NotNull ColumnConstraintType constraintType) {
        this.databaseName = databaseName;
        this.tableName = tableName;
        this.columnName = columnName;
        this.constraintType = constraintType;
    }

    @Override
    public String getMessage() {
        return "Constraint violation for the column "
                + Stream.of(databaseName, tableName, columnName)
                .collect(Collectors.joining(".")) +
                ": " + constraintType;
    }


    @NotNull
    public ColumnConstraintType getConstraintType() {
        return constraintType;
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
