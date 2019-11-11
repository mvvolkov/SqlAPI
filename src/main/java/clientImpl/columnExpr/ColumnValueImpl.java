package clientImpl.columnExpr;

import api.columnExpr.ColumnValue;

public class ColumnValueImpl<T extends Comparable<T>> extends ColumnExprImpl implements ColumnValue<T> {

    private final T value;

    private final Class<T> javaClass;

    public ColumnValueImpl(T value, Class<T> javaClass) {
        super(Type.COLUMN_VALUE);
        this.value = value;
        this.javaClass = javaClass;
    }

    @Override
    public T getValue() {
        return value;
    }

    public Class<T> getJavaClass() {
        return javaClass;
    }
}
