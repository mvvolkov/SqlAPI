package clientImpl.columnExpr;

import api.columnExpr.BinaryColumnExpression;
import api.columnExpr.ColumnExpression;
import api.columnExpr.ColumnValue;
import org.jetbrains.annotations.NotNull;

public class ColumnExprFactory {

    private ColumnExprFactory() {
    }

    public static ColumnValue value(Object value, String alias) {
        return new ColumnValueImpl(value, alias);
    }

    public static ColumnValue value(Object value) {
        return new ColumnValueImpl(value, "");
    }

    public static ColumnRefImpl columnRefWithAlias(@NotNull String schemaName, @NotNull String tableName,
                                                   @NotNull String columnName, @NotNull String alias) {
        return new ColumnRefImpl(schemaName, tableName, columnName, alias);
    }

    public static ColumnRefImpl columnRefWithAlias(@NotNull String tableName,
                                                   String columnName, @NotNull String alias) {
        return new ColumnRefImpl(tableName, columnName, alias);
    }

    public static ColumnRefImpl columnRefWithAlias(@NotNull String columnName, @NotNull String alias) {
        return new ColumnRefImpl(columnName, alias);
    }

    public static ColumnRefImpl columnRef(@NotNull String schemaName, @NotNull String tableName,
                                          @NotNull String columnName) {
        return new ColumnRefImpl(schemaName, tableName, columnName, "");
    }

    public static ColumnRefImpl columnRef(@NotNull String tableName, @NotNull String columnName) {
        return new ColumnRefImpl(tableName, columnName, "");
    }

    public static ColumnRefImpl columnRef(@NotNull String columnName) {
        return new ColumnRefImpl(columnName, "");
    }


    public static ColumnValue integer(Integer value, String alias) {
        return new ColumnValueImpl(value, alias);
    }

    public static ColumnValue integer(Integer value) {
        return new ColumnValueImpl(value, "");
    }

    public static ColumnValue string(String value, String alias) {
        return new ColumnValueImpl(value, alias);
    }

    public static ColumnValue string(String value) {
        return new ColumnValueImpl(value, "");
    }


    public static BinaryColumnExpression sum(ColumnExpression leftOperand,
                                             ColumnExpression rightOperand,
                                             String alias) {
        return new BinaryColumnExprImpl(ColumnExpression.ExprType.SUM, leftOperand,
                rightOperand, alias);
    }

    public static BinaryColumnExpression sum(ColumnExpression leftOperand,
                                             ColumnExpression rightOperand) {
        return sum(leftOperand, rightOperand, "");
    }

    public static BinaryColumnExpression diff(ColumnExpression leftOperand,
                                              ColumnExpression rightOperand,
                                              String alias) {
        return new BinaryColumnExprImpl(ColumnExpression.ExprType.DIFF, leftOperand,
                rightOperand, alias);
    }

    public static BinaryColumnExpression diff(ColumnExpression leftOperand,
                                              ColumnExpression rightOperand) {
        return diff(leftOperand, rightOperand, "");
    }

    public static BinaryColumnExpression product(ColumnExpression leftOperand,
                                                 ColumnExpression rightOperand,
                                                 String alias) {
        return new BinaryColumnExprImpl(ColumnExpression.ExprType.PRODUCT, leftOperand,
                rightOperand, alias);
    }

    public static BinaryColumnExpression product(ColumnExpression leftOperand,
                                                 ColumnExpression rightOperand) {
        return product(leftOperand, rightOperand, "");
    }


    public static BinaryColumnExpression divide(ColumnExpression leftOperand,
                                                ColumnExpression rightOperand,
                                                String alias) {
        return new BinaryColumnExprImpl(ColumnExpression.ExprType.DIVIDE, leftOperand,
                rightOperand, alias);
    }

    public static BinaryColumnExpression divide(ColumnExpression leftOperand,
                                                ColumnExpression rightOperand) {
        return divide(leftOperand, rightOperand, "");
    }
}
