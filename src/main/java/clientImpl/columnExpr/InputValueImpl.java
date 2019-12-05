package clientImpl.columnExpr;

import org.jetbrains.annotations.NotNull;
import sqlapi.columnExpr.InputValue;
import org.jetbrains.annotations.Nullable;

class InputValueImpl extends ColumnExprImpl implements InputValue {

    @Nullable
    protected Object value;


    InputValueImpl(@Nullable Object value, @NotNull String alias) {
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
