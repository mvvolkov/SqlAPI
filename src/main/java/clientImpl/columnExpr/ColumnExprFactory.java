package clientImpl.columnExpr;

import api.columnExpr.BinaryColumnExpression;
import api.columnExpr.ColumnExpression;
import api.columnExpr.ColumnRef;
import api.columnExpr.ColumnValue;

import java.util.List;
import java.util.stream.Collectors;

public class ColumnExprFactory {

    private ColumnExprFactory() {
    }

    public static ColumnValue nullValue() {
        return new ColumnValueImpl(null);
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

    public static ColumnValue columnValue(Object value) {
        return new ColumnValueImpl(value);
    }

    public static List<ColumnValue> columnValues(List<Object> values) {
        return values.stream().map(value -> new ColumnValueImpl(value))
                .collect(Collectors.toList());
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
