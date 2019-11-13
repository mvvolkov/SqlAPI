package clientImpl.columnExpr;

import api.columnExpr.ColumnValue;

public final class ColumnValueImpl extends ColumnExprImpl implements ColumnValue {

    private final Object value;


    public ColumnValueImpl(Object value) {
        super(ExprType.COLUMN_VALUE);
        this.value = value;
    }

    @Override
    public Object getValue() {
        return value;
    }
}
