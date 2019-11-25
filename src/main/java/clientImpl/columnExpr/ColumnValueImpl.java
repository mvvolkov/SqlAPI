package clientImpl.columnExpr;

import org.jetbrains.annotations.NotNull;
import sqlapi.columnExpr.ColumnValue;
import org.jetbrains.annotations.Nullable;

final class ColumnValueImpl extends ColumnExprImpl implements ColumnValue {

    @Nullable
    private final Object value;


    ColumnValueImpl(@Nullable Object value, @NotNull String alias) {
        super(ExprType.COLUMN_VALUE, alias);
        this.value = value;
    }

    @Nullable
    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return (value instanceof String) ? "'" + value + "'" : String.valueOf(value);
    }
}
