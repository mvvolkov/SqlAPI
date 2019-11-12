package api.exceptions;

import api.columnExpr.ColumnRef;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class WrongValueTypeException extends SqlException {


    @NotNull
    private final ColumnRef columnRef;

    @NotNull
    private final Class expectedClass;

    @NotNull
    private final Class actualClass;

    public WrongValueTypeException(ColumnRef columnRef, Class expectedClass, Class actualClass) {
        this.columnRef = columnRef;
        this.expectedClass = expectedClass;
        this.actualClass = actualClass;
    }

    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("Wrong value type for the column ");
        String fullName = Stream.of(columnRef.getDatabaseName(), columnRef.getTableName(), columnRef.getColumnName())
                .collect(Collectors.joining("."));
        sb.append(fullName);
        sb.append("; Expected: ");
        sb.append(expectedClass.getSimpleName());
        sb.append("; Actual: ");
        sb.append(actualClass.getSimpleName());
        return sb.toString();
    }

    public ColumnRef getColumnRef() {
        return columnRef;
    }

    public Class getExpectedClass() {
        return expectedClass;
    }

    public Class getActualClass() {
        return actualClass;
    }
}
