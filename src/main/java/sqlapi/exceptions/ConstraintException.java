package sqlapi.exceptions;

import sqlapi.ColumnReference;

import java.sql.SQLException;

public class ConstraintException extends SQLException {

    private final ColumnReference columnReference;

    public ConstraintException(ColumnReference columnReference) {
        this.columnReference = columnReference;
    }

    @Override
    public String getMessage() {
        return "Constraint violation for the column " + columnReference;
    }
}
