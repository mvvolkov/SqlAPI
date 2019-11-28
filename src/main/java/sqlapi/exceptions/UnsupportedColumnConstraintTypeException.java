package sqlapi.exceptions;

import sqlapi.metadata.ColumnConstraintType;

public class UnsupportedColumnConstraintTypeException extends SqlException {

    private final ColumnConstraintType type;


    public UnsupportedColumnConstraintTypeException(ColumnConstraintType type) {
        this.type = type;
    }

    public ColumnConstraintType getColumnConstraintType() {
        return type;
    }

    @Override
    public String getMessage() {
        return "Unsupported column constraint type: " + type;
    }
}
