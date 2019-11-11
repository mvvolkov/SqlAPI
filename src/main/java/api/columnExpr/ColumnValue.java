package api.columnExpr;

public interface ColumnValue<T extends Comparable<T>> extends ColumnExpression {

    T getValue();

    Class<T> getJavaClass();

    @Override
    default Type getType() {
        return Type.COLUMN_VALUE;
    }
}
