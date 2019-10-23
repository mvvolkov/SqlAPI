package sqlapi.exceptions;

import sqlapi.ColumnReference;

public class WrongValueTypeException extends SqlException {

    private final ColumnReference columnReference;

    private final Class expectedClass;

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
}
