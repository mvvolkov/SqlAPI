package clientImpl.columnExpr;

import org.jetbrains.annotations.NotNull;
import sqlapi.columnExpr.ColumnValue;
import org.jetbrains.annotations.Nullable;

final class ColumnValueImpl extends ColumnExprImpl implements ColumnValue {

    @Nullable
    private final Object value;


    ColumnValueImpl(@Nullable Object value, @NotNull String alias) {
        super(alias);
        this.value = value;
    }

    @Nullable
    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        if (value instanceof String) {
            return '\'' + (String) value + '\'';
        }
        if (value == null) {
            return "NULL";
        }
        return String.valueOf(value);
    }
}
