package sqlapi.exceptions;

import org.jetbrains.annotations.NotNull;
import sqlapi.metadata.ColumnConstraintType;

public final class UnsupportedColumnConstraintTypeException extends SqlException {

    @NotNull
    private final ColumnConstraintType type;


    public UnsupportedColumnConstraintTypeException(@NotNull ColumnConstraintType type) {
        this.type = type;
    }

    @NotNull
    public ColumnConstraintType getColumnConstraintType() {
        return type;
    }

    @Override
    public String getMessage() {
        return "Unsupported column constraint type: " + type;
    }
}
