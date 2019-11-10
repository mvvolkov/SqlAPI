package api.columnExpr;

public interface ColumnValue extends ColumnExpression {

    Object getValue();

    @Override default Type getType() {
        return Type.COLUMN_VALUE;
    }
}
