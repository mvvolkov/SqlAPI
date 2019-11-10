package api.columnExpr;

public interface ColumnExpression {

    enum Type {
        COLUMN_VALUE,
        COLUMN_REF,
        NULL_VALUE,
        ADD,
        SUBTRACT,
        MULTIPLY,
        DIVIDE
    }

    Type getType();
}
