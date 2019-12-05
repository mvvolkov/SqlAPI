package sqlapi.exceptions;

import org.jetbrains.annotations.NotNull;
import sqlapi.metadata.SqlType;

public final class UnsupportedSqlTypeException extends SqlException {

    @NotNull
    private final SqlType sqlType;

    public UnsupportedSqlTypeException(@NotNull SqlType sqlType) {
        this.sqlType = sqlType;
    }

    @NotNull
    public SqlType getSqlType() {
        return sqlType;
    }

    @Override
    public String getMessage() {
        return "Unsupported SQL type " + sqlType;
    }
}
