package clientImpl.columnExpr;

import api.columnExpr.BinaryColumnExpression;
import api.columnExpr.ColumnExpression;
import api.columnExpr.ColumnRef;
import api.columnExpr.ColumnValue;

public class ColumnExprFactory {

    private ColumnExprFactory() {
    }

    public static ColumnValue nullValue() {
        return new ColumnValueImpl(null, null);
    }

    public static ColumnRef columnRef(String columnName) {
        return new ColumnRefImpl(columnName);
    }

    public static ColumnRef columnRef(String tableName, String columnName) {
        return new ColumnRefImpl(tableName, columnName);
    }

    public static ColumnRef columnRef(String databaseName, String tableName,
                                      String columnName) {
        return new ColumnRefImpl(databaseName, tableName, columnName);
    }

    public static ColumnValue<Integer> integer(Integer value) {
        return new ColumnValueImpl(value, Integer.class);
    }

    public static ColumnValue<Integer> string(String value) {
        return new ColumnValueImpl(value, String.class);
    }


    public static BinaryColumnExpression add(ColumnExpression leftOperand,
                                             ColumnExpression rightOperand) {
        return new BinaryColumnExprImpl(ColumnExpression.Type.ADD, leftOperand,
                rightOperand);
    }

    public static BinaryColumnExpression subtract(ColumnExpression leftOperand,
                                                  ColumnExpression rightOperand) {
        return new BinaryColumnExprImpl(ColumnExpression.Type.SUBTRACT, leftOperand,
                rightOperand);
    }

    public static BinaryColumnExpression multiply(ColumnExpression leftOperand,
                                                  ColumnExpression rightOperand) {
        return new BinaryColumnExprImpl(ColumnExpression.Type.MULTIPLY, leftOperand,
                rightOperand);
    }

    public static BinaryColumnExpression divide(ColumnExpression leftOperand,
                                                ColumnExpression rightOperand) {
        return new BinaryColumnExprImpl(ColumnExpression.Type.DIVIDE, leftOperand,
                rightOperand);
    }
}
