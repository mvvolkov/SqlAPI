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

    public static ColumnRefImpl columnRefWithAlias(String schemaName, String tableName,
                                                   String columnName, String alias) {
        return new ColumnRefImpl(schemaName, tableName, columnName, alias);
    }

    public static ColumnRefImpl columnRefWithAlias(String tableName,
                                                   String columnName, String alias) {
        return new ColumnRefImpl(tableName, columnName, alias);
    }

    public static ColumnRefImpl columnRefWithAlias(String columnName, String alias) {
        return new ColumnRefImpl(columnName, alias);
    }

    public static ColumnRefImpl columnRef(String schemaName, String tableName,
                                          String columnName) {
        return new ColumnRefImpl(schemaName, tableName, columnName, null);
    }

    public static ColumnRefImpl columnRef(String tableName, String columnName) {
        return new ColumnRefImpl(tableName, columnName, null);
    }

    public static ColumnRefImpl columnRef(String columnName) {
        return new ColumnRefImpl(columnName, null);
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
