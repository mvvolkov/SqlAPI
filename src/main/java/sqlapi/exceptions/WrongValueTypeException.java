package sqlapi.exceptions;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class WrongValueTypeException extends SqlException {


    @NotNull
    private final String tableName;

    @NotNull
    private final String columnName;

    @NotNull
    private final Collection<Class<?>> allowedTypes;

    @NotNull
    private final Class<?> actualType;

    public WrongValueTypeException(@NotNull String tableName,
                                   @NotNull String columnName,
                                   @NotNull Collection<Class<?>> allowedTypes,
                                   @NotNull Class actualType) {
        this.tableName = tableName;
        this.columnName = columnName;
        this.allowedTypes = allowedTypes;
        this.actualType = actualType;
    }

    public WrongValueTypeException() {
        this.tableName = null;
        this.columnName = null;
        this.allowedTypes = null;
        this.actualType = null;
    }

    @Override
    public String getMessage() {
        if (tableName == null) {
            return "message to be done";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Wrong value type for the column ");
        String fullName = String.join(".", tableName,
                columnName);
        sb.append(fullName);
        sb.append("; Expected: ");
        List<String> classNames = new ArrayList<>();
        for (Class<?> cl : allowedTypes) {
            classNames.add(cl.getSimpleName());
        }
        sb.append(String.join(", ", classNames));
        sb.append("; Actual: ");
        sb.append(actualType.getSimpleName());
        return sb.toString();
    }


    @NotNull public Collection<Class<?>> getAllowedTypes() {
        return allowedTypes;
    }

    @NotNull
    public Class getActualType() {
        return actualType;
    }


    @NotNull public String getTableName() {
        return tableName;
    }

    @NotNull public String getColumnName() {
        return columnName;
    }
}
