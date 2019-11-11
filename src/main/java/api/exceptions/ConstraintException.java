package api.exceptions;

import api.columnExpr.ColumnRef;
import org.jetbrains.annotations.NotNull;

public final class ConstraintException extends SqlException {

    @NotNull
    private final ColumnRef columnRef;

    @NotNull
    private final String errorMessage;

    public ConstraintException(@NotNull ColumnRef columnRef, @NotNull String errorMessage) {
        this.columnRef = columnRef;
        this.errorMessage = errorMessage;
    }

    @Override
    public String getMessage() {
        return "Constraint violation for the column " + columnRef.getDatabaseName()
                + "." + columnRef.getTableName() + "." + columnRef.getColumnName() + ": " + errorMessage;
    }

    @NotNull
    public ColumnRef getColumnRef() {
        return columnRef;
    }

    @NotNull
    public String getErrorMessage() {
        return errorMessage;
    }
}
