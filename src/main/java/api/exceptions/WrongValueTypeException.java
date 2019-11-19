package api.exceptions;

import api.columnExpr.ColumnRef;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class WrongValueTypeException extends SqlException {


    @NotNull
    private final ColumnRef columnRef;

    @NotNull
    private final Collection<Class<?>> allowedTypes;

    @NotNull
    private final Class<?> actualType;

    public WrongValueTypeException(ColumnRef columnRef, Collection<Class<?>> allowedTypes, Class actualType) {
        this.columnRef = columnRef;
        this.allowedTypes = allowedTypes;
        this.actualType = actualType;
    }

    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("Wrong value type for the column ");
        String fullName = Stream.of(columnRef.getSchemaName(), columnRef.getTableName(), columnRef.getColumnName())
                .collect(Collectors.joining("."));
        sb.append(fullName);
        sb.append("; Expected: ");
        List<String> classNames = new ArrayList<>();
        for (Class<?> cl : allowedTypes) {
            classNames.add(cl.getSimpleName());
        }
        sb.append(classNames.stream().collect(Collectors.joining(", ")));
        sb.append("; Actual: ");
        sb.append(actualType.getSimpleName());
        return sb.toString();
    }

    public ColumnRef getColumnRef() {
        return columnRef;
    }

    public Collection<Class<?>> getAllowedTypes() {
        return allowedTypes;
    }

    public Class getActualType() {
        return actualType;
    }
}
