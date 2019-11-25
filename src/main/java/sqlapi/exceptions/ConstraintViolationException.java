package sqlapi.exceptions;

import org.jetbrains.annotations.NotNull;
import sqlapi.metadata.ColumnConstraintType;


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
            @NotNull String databaseName, @NotNull String tableName,
            @NotNull String columnName,
            @NotNull ColumnConstraintType constraintType) {
        this.databaseName = databaseName;
        this.tableName = tableName;
        this.columnName = columnName;
        this.constraintType = constraintType;
    }

    @Override
    public String getMessage() {
        return "Constraint violation for the column "
                + String.join(".", databaseName, tableName, columnName) +
                ": " + constraintType;
    }


    @NotNull
    public ColumnConstraintType getConstraintType() {
        return constraintType;
    }

    @NotNull
    public String getDatabaseName() {
        return databaseName;
    }

    @NotNull
    public String getTableName() {
        return tableName;
    }

    @NotNull
    public String getColumnName() {
        return columnName;
    }
}
