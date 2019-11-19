package sqlapi.exceptions;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class WrongValueTypeException extends SqlException {


    @NotNull
    private final String schemaName;

    @NotNull
    private final String tableName;


    @NotNull
    private final String columnName;

    @NotNull
    private final Collection<Class<?>> allowedTypes;

    @NotNull
    private final Class<?> actualType;

    public WrongValueTypeException(String schemaName, String tableName,
                                   String columnName, Collection<Class<?>> allowedTypes,
                                   Class actualType) {
        this.schemaName = schemaName;
        this.tableName = tableName;
        this.columnName = columnName;
        this.allowedTypes = allowedTypes;
        this.actualType = actualType;
    }

    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("Wrong value type for the column ");
        String fullName = Stream.of(schemaName, tableName,
                columnName)
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


    public Collection<Class<?>> getAllowedTypes() {
        return allowedTypes;
    }

    public Class getActualType() {
        return actualType;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public String getTableName() {
        return tableName;
    }

    public String getColumnName() {
        return columnName;
    }
}
