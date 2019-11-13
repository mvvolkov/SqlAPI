package clientImpl.columnExpr;

import api.columnExpr.BinaryColumnExpression;
import api.columnExpr.ColumnExpression;
import api.columnExpr.ColumnValue;

public class ColumnExprFactory {

    private ColumnExprFactory() {
    }

    public static ColumnValue value(Object value, String alias) {
        return new ColumnValueImpl(value, alias);
    }

    public static ColumnValue value(Object value) {
        return new ColumnValueImpl(value, null);
    }

    public static ColumnValue nullValue(String alias) {
        return value(null, alias);
    }

    public static ColumnValue nullValue() {
        return value(null, null);
    }


    public static ColumnRefImpl columnRef(String databaseName, String tableName,
                                          String columnName, String alias) {
        return new ColumnRefImpl(databaseName, tableName, columnName, alias);
    }

    public static ColumnRefImpl columnRef(String databaseName, String tableName,
                                          String columnName) {
        return new ColumnRefImpl(databaseName, tableName, columnName, columnName);
    }

    public static ColumnValue integer(Integer value, String alias) {
        return new ColumnValueImpl(value, alias);
    }

    public static ColumnValue integer(Integer value) {
        return new ColumnValueImpl(value, null);
    }

    public static ColumnValue string(String value, String alias) {
        return new ColumnValueImpl(value, alias);
    }

    public static ColumnValue string(String value) {
        return new ColumnValueImpl(value, null);
    }


    public static BinaryColumnExpression sum(ColumnExpression leftOperand,
                                             ColumnExpression rightOperand,
                                             String alias) {
        return new BinaryColumnExprImpl(ColumnExpression.ExprType.SUM, leftOperand,
                rightOperand, alias);
    }

    public static BinaryColumnExpression sum(ColumnExpression leftOperand,
                                             ColumnExpression rightOperand) {
        return sum(leftOperand, rightOperand, null);
    }

    public static BinaryColumnExpression diff(ColumnExpression leftOperand,
                                              ColumnExpression rightOperand,
                                              String alias) {
        return new BinaryColumnExprImpl(ColumnExpression.ExprType.DIFF, leftOperand,
                rightOperand, alias);
    }

    public static BinaryColumnExpression diff(ColumnExpression leftOperand,
                                              ColumnExpression rightOperand) {
        return diff(leftOperand, rightOperand, null);
    }

    public static BinaryColumnExpression product(ColumnExpression leftOperand,
                                                 ColumnExpression rightOperand,
                                                 String alias) {
        return new BinaryColumnExprImpl(ColumnExpression.ExprType.PRODUCT, leftOperand,
                rightOperand, alias);
    }

    public static BinaryColumnExpression product(ColumnExpression leftOperand,
                                                 ColumnExpression rightOperand) {
        return product(leftOperand, rightOperand, null);
    }


    public static BinaryColumnExpression divide(ColumnExpression leftOperand,
                                                ColumnExpression rightOperand,
                                                String alias) {
        return new BinaryColumnExprImpl(ColumnExpression.ExprType.DIVIDE, leftOperand,
                rightOperand, alias);
    }

    public static BinaryColumnExpression divide(ColumnExpression leftOperand,
                                                ColumnExpression rightOperand) {
        return divide(leftOperand, rightOperand, null);
    }
}
