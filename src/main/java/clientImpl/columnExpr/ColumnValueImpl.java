package clientImpl.columnExpr;

import api.columnExpr.ColumnValue;

public class ColumnValueImpl extends ColumnExprImpl implements ColumnValue {

    private final Object value;

    public ColumnValueImpl(Object value) {
        super(Type.COLUMN_VALUE);
        this.value = value;
    }

    @Override public Object getValue() {
        return value;
    }
}
