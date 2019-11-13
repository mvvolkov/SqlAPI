package clientImpl.columnExpr;

import api.columnExpr.ColumnValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ColumnValueImpl extends ColumnExprImpl implements ColumnValue {

    @NotNull
    private final Object value;


    public ColumnValueImpl(@NotNull Object value, @Nullable String alias) {
        super(ExprType.COLUMN_VALUE, alias);
        this.value = value;
    }

    @NotNull
    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return (value instanceof String) ? "'" + value + "'" : String.valueOf(value);
    }
}
