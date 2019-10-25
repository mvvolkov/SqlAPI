package sqlapi.exceptions;

import sqlapi.ColumnReference;

public class ConstraintException extends SqlException {

    private final ColumnReference columnReference;

    private final String errorMessage;

    public ConstraintException(ColumnReference columnReference, String errorMessage) {
        this.columnReference = columnReference;
        this.errorMessage = errorMessage;
    }

    @Override
    public String getMessage() {
        return "Constraint violation for the column " + columnReference + ": " + errorMessage;
    }
}
