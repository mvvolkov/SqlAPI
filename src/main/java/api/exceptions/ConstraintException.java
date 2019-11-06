package api.exceptions;

import api.ColumnReference;
import org.jetbrains.annotations.NotNull;
import clientDefaultImpl.ColumnReferenceImpl;

public final class ConstraintException extends SqlException {

    @NotNull
    private final ColumnReference columnReference;

    @NotNull
    private final String errorMessage;

    public ConstraintException(@NotNull ColumnReference columnReference, @NotNull String errorMessage) {
        this.columnReference = columnReference;
        this.errorMessage = errorMessage;
    }

    @Override
    public String getMessage() {
        return "Constraint violation for the column " + columnReference + ": " + errorMessage;
    }

    @NotNull
    public ColumnReference getColumnReference() {
        return columnReference;
    }

    @NotNull
    public String getErrorMessage() {
        return errorMessage;
    }
}
