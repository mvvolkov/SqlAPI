package clientImpl.columnExpr;

import api.columnExpr.*;
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

    public static ColumnRefImpl columnRefWithAlias(@NotNull String schemaName,
                                                   @NotNull String tableName,
                                                   @NotNull String columnName,
                                                   @NotNull String alias) {
        return new ColumnRefImpl(schemaName, tableName, columnName, alias);
    }

    public static ColumnRefImpl columnRefWithAlias(@NotNull String tableName,
                                                   String columnName,
                                                   @NotNull String alias) {
        return new ColumnRefImpl(tableName, columnName, alias);
    }

    public static ColumnRefImpl columnRefWithAlias(@NotNull String columnName,
                                                   @NotNull String alias) {
        return new ColumnRefImpl(columnName, alias);
    }

    public static ColumnRefImpl columnRef(@NotNull String schemaName,
                                          @NotNull String tableName,
                                          @NotNull String columnName) {
        return new ColumnRefImpl(schemaName, tableName, columnName, "");
    }

    public static ColumnRefImpl columnRef(@NotNull String tableName,
                                          @NotNull String columnName) {
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

    public static AggregateFunction countAll(String alias) {
        return aggregateFunction(AggregateFunction.Type.COUNT, "", alias);
    }

    public static AggregateFunction countAll() {
        return aggregateFunction(AggregateFunction.Type.COUNT, "");
    }

    public static AggregateFunction count(String columnName, String alias) {
        return aggregateFunction(AggregateFunction.Type.COUNT, columnName, alias);
    }

    public static AggregateFunction count(String columnName) {
        return aggregateFunction(AggregateFunction.Type.COUNT, columnName);
    }

    public static AggregateFunction count(ColumnRef columnRef, String alias) {
        return aggregateFunction(AggregateFunction.Type.COUNT, columnRef, alias);
    }

    public static AggregateFunction count(ColumnRef columnRef) {
        return aggregateFunction(AggregateFunction.Type.COUNT, columnRef);
    }

    public static AggregateFunction aggregateSum(String columnName, String alias) {
        return aggregateFunction(AggregateFunction.Type.SUM, columnName, alias);
    }

    public static AggregateFunction aggregateSum(String columnName) {
        return aggregateFunction(AggregateFunction.Type.SUM, columnName);
    }

    public static AggregateFunction aggregateSum(ColumnRef columnRef, String alias) {
        return aggregateFunction(AggregateFunction.Type.SUM, columnRef, alias);
    }

    public static AggregateFunction aggregateSum(ColumnRef columnRef) {
        return aggregateFunction(AggregateFunction.Type.SUM, columnRef);
    }

    public static AggregateFunction avg(String columnName, String alias) {
        return aggregateFunction(AggregateFunction.Type.AVG, columnName, alias);
    }

    public static AggregateFunction avg(String columnName) {
        return aggregateFunction(AggregateFunction.Type.AVG, columnName);
    }

    public static AggregateFunction avg(ColumnRef columnRef, String alias) {
        return aggregateFunction(AggregateFunction.Type.AVG, columnRef, alias);
    }

    public static AggregateFunction avg(ColumnRef columnRef) {
        return aggregateFunction(AggregateFunction.Type.AVG, columnRef);
    }

    public static AggregateFunction max(String columnName, String alias) {
        return aggregateFunction(AggregateFunction.Type.MAX, columnName, alias);
    }

    public static AggregateFunction max(String columnName) {
        return aggregateFunction(AggregateFunction.Type.MAX, columnName);
    }

    public static AggregateFunction max(ColumnRef columnRef, String alias) {
        return aggregateFunction(AggregateFunction.Type.MAX, columnRef, alias);
    }

    public static AggregateFunction max(ColumnRef columnRef) {
        return aggregateFunction(AggregateFunction.Type.MAX, columnRef);
    }

    public static AggregateFunction min(String columnName, String alias) {
        return aggregateFunction(AggregateFunction.Type.MIN, columnName, alias);
    }

    public static AggregateFunction min(String columnName) {
        return aggregateFunction(AggregateFunction.Type.MIN, columnName);
    }

    public static AggregateFunction min(ColumnRef columnRef, String alias) {
        return aggregateFunction(AggregateFunction.Type.MIN, columnRef, alias);
    }

    public static AggregateFunction min(ColumnRef columnRef) {
        return aggregateFunction(AggregateFunction.Type.MIN, columnRef);
    }

    private static AggregateFunction aggregateFunction(AggregateFunction.Type type,
                                                       ColumnRef columnRef) {
        return new AggregateFunctionImpl(columnRef, type, "");
    }

    private static AggregateFunction aggregateFunction(AggregateFunction.Type type,
                                                       String columnName) {
        return aggregateFunction(type, columnRef(columnName));
    }

    private static AggregateFunction aggregateFunction(AggregateFunction.Type type,
                                                       ColumnRef columnRef,
                                                       String alias) {
        return new AggregateFunctionImpl(columnRef, type, alias);
    }

    private static AggregateFunction aggregateFunction(AggregateFunction.Type type,
                                                       String columnName, String alias) {
        return aggregateFunction(type, columnRef(columnName), alias);
    }


}
