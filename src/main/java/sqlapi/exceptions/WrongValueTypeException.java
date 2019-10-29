package sqlapi.exceptions;

import org.jetbrains.annotations.NotNull;
import sqlapi.ColumnReference;

public final class WrongValueTypeException extends SqlException {


    @NotNull
    private final ColumnReference columnReference;

    @NotNull
    private final Class expectedClass;

    @NotNull
    private final Class actualClass;

    public WrongValueTypeException(ColumnReference columnReference, Class expectedClass, Class actualClass) {
        this.columnReference = columnReference;
        this.expectedClass = expectedClass;
        this.actualClass = actualClass;
    }

    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("Wrong value type for the column ");
        sb.append(columnReference.toString());
        sb.append("; Expected: ");
        sb.append(expectedClass.getSimpleName());
        sb.append("; Actual: ");
        sb.append(actualClass.getSimpleName());
        return sb.toString();
    }

    public ColumnReference getColumnReference() {
        return columnReference;
    }

    public Class getExpectedClass() {
        return expectedClass;
    }

    public Class getActualClass() {
        return actualClass;
    }
}
