package clientImpl.columnExpr;

import sqlapi.columnExpr.*;
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

    public static ColumnRef columnRefWithAlias(@NotNull String schemaName,
                                               @NotNull String tableName,
                                               @NotNull String columnName,
                                               @NotNull String alias) {
        return new ColumnRefImpl(schemaName, tableName, columnName, alias);
    }

    public static ColumnRef columnRefWithAlias(@NotNull String tableName,
                                               String columnName,
                                               @NotNull String alias) {
        return new ColumnRefImpl(tableName, columnName, alias);
    }

    public static ColumnRef columnRefWithAlias(@NotNull String columnName,
                                               @NotNull String alias) {
        return new ColumnRefImpl(columnName, alias);
    }

    public static ColumnRef columnRef(@NotNull String schemaName,
                                      @NotNull String tableName,
                                      @NotNull String columnName) {
        return new ColumnRefImpl(schemaName, tableName, columnName, "");
    }

    public static ColumnRef columnRef(@NotNull String tableName,
                                      @NotNull String columnName) {
        return new ColumnRefImpl(tableName, columnName, "");
    }

    public static ColumnRef columnRef(@NotNull String columnName) {
        return new ColumnRefImpl(columnName, "");
    }


    public static ColumnExpression sumWithAlias(ColumnExpression leftOperand,
                                                ColumnExpression rightOperand,
                                                String alias) {
        return new BinaryColumnExprImpl(ColumnExpression.ExprType.SUM, leftOperand,
                rightOperand, alias);
    }

    public static ColumnExpression sumWithAlias(ColumnExpression leftOperand,
                                                String columnName,
                                                String alias) {
        return sumWithAlias(leftOperand, columnRef(columnName), alias);
    }

    public static ColumnExpression sumWithAlias(ColumnExpression leftOperand,
                                                String tableName,
                                                String columnName,
                                                String alias) {
        return sumWithAlias(leftOperand, columnRef(tableName, columnName), alias);
    }

    public static ColumnExpression sumWithAlias(String columnName,
                                                ColumnExpression rightOperand,
                                                String alias) {
        return sumWithAlias(columnRef(columnName), rightOperand, alias);
    }

    public static ColumnExpression sumWithAlias(String tableName, String columnName,
                                                ColumnExpression rightOperand,
                                                String alias) {
        return sumWithAlias(columnRef(tableName, columnName), rightOperand, alias);
    }

    public static ColumnExpression sumWithAlias(String columnName1,
                                                String columnName2,
                                                String alias) {
        return sumWithAlias(columnRef(columnName1), columnRef(columnName2), alias);
    }

    public static ColumnExpression sumWithAlias(String tableName1, String columnName1,
                                                String tableName2, String columnName2,
                                                String alias) {
        return sumWithAlias(columnRef(tableName1, columnName1), columnRef(tableName2, columnName2), alias);
    }


    public static ColumnExpression sum(ColumnExpression leftOperand,
                                       ColumnExpression rightOperand) {
        return sumWithAlias(leftOperand, rightOperand, "");
    }

    public static ColumnExpression sum(ColumnExpression leftOperand,
                                       String columnName) {
        return sum(leftOperand, columnRef(columnName));
    }

    public static ColumnExpression sum(ColumnExpression leftOperand, String tableName,
                                       String columnName) {
        return sum(leftOperand, columnRef(tableName, columnName));
    }

    public static ColumnExpression sum(String columnName, ColumnExpression rightOperand) {
        return sum(columnRef(columnName), rightOperand);
    }

    public static ColumnExpression sum(String tableName, String columnName, ColumnExpression rightOperand) {
        return sum(columnRef(tableName, columnName), rightOperand);
    }

    public static ColumnExpression sum(String columnName1, String columnName2) {
        return sum(columnRef(columnName1), columnRef(columnName2));
    }

    public static ColumnExpression sum(String tableName1, String columnName1,
                                       String tableName2, String columnName2) {
        return sum(columnRef(tableName1, columnName1), columnRef(tableName2, columnName2));
    }


    public static ColumnExpression diffWithAlias(ColumnExpression leftOperand,
                                        ColumnExpression rightOperand,
                                        String alias) {
        return new BinaryColumnExprImpl(ColumnExpression.ExprType.DIFF, leftOperand,
                rightOperand, alias);
    }

    public static ColumnExpression diffWithAlias(ColumnExpression leftOperand,
                                                String columnName,
                                                String alias) {
        return diffWithAlias(leftOperand, columnRef(columnName), alias);
    }

    public static ColumnExpression diffWithAlias(ColumnExpression leftOperand,
                                                String tableName,
                                                String columnName,
                                                String alias) {
        return diffWithAlias(leftOperand, columnRef(tableName, columnName), alias);
    }

    public static ColumnExpression diffWithAlias(String columnName,
                                                ColumnExpression rightOperand,
                                                String alias) {
        return diffWithAlias(columnRef(columnName), rightOperand, alias);
    }

    public static ColumnExpression diffWithAlias(String tableName, String columnName,
                                                ColumnExpression rightOperand,
                                                String alias) {
        return diffWithAlias(columnRef(tableName, columnName), rightOperand, alias);
    }

    public static ColumnExpression diffWithAlias(String columnName1,
                                                String columnName2,
                                                String alias) {
        return diffWithAlias(columnRef(columnName1), columnRef(columnName2), alias);
    }

    public static ColumnExpression diffWithAlias(String tableName1, String columnName1,
                                                String tableName2, String columnName2,
                                                String alias) {
        return diffWithAlias(columnRef(tableName1, columnName1), columnRef(tableName2, columnName2), alias);
    }

    public static ColumnExpression diff(ColumnExpression leftOperand,
                                        ColumnExpression rightOperand) {
        return diffWithAlias(leftOperand, rightOperand, "");
    }

    public static ColumnExpression diff(ColumnExpression leftOperand,
                                       String columnName) {
        return diff(leftOperand, columnRef(columnName));
    }

    public static ColumnExpression diff(ColumnExpression leftOperand, String tableName,
                                       String columnName) {
        return diff(leftOperand, columnRef(tableName, columnName));
    }

    public static ColumnExpression diff(String columnName, ColumnExpression rightOperand) {
        return diff(columnRef(columnName), rightOperand);
    }

    public static ColumnExpression diff(String tableName, String columnName, ColumnExpression rightOperand) {
        return diff(columnRef(tableName, columnName), rightOperand);
    }

    public static ColumnExpression diff(String columnName1, String columnName2) {
        return diff(columnRef(columnName1), columnRef(columnName2));
    }

    public static ColumnExpression diff(String tableName1, String columnName1,
                                       String tableName2, String columnName2) {
        return diff(columnRef(tableName1, columnName1), columnRef(tableName2, columnName2));
    }

    public static ColumnExpression product(ColumnExpression leftOperand,
                                           ColumnExpression rightOperand,
                                           String alias) {
        return new BinaryColumnExprImpl(ColumnExpression.ExprType.PRODUCT, leftOperand,
                rightOperand, alias);
    }

    public static ColumnExpression product(ColumnExpression leftOperand,
                                           ColumnExpression rightOperand) {
        return product(leftOperand, rightOperand, "");
    }


    public static ColumnExpression divide(ColumnExpression leftOperand,
                                          ColumnExpression rightOperand,
                                          String alias) {
        return new BinaryColumnExprImpl(ColumnExpression.ExprType.DIVIDE, leftOperand,
                rightOperand, alias);
    }

    public static ColumnExpression divide(ColumnExpression leftOperand,
                                          ColumnExpression rightOperand) {
        return divide(leftOperand, rightOperand, "");
    }

    public static AggregateFunction countAll(String alias) {
        return aggregateFunctionWithAlias(AggregateFunction.Type.COUNT, "", alias);
    }

    public static AggregateFunction countAll() {
        return aggregateFunction(AggregateFunction.Type.COUNT, "");
    }

    public static AggregateFunction countWithAlias(String columnName, String alias) {
        return aggregateFunctionWithAlias(AggregateFunction.Type.COUNT, columnName, alias);
    }

    public static AggregateFunction countWithAlias(String tableName, String columnName, String alias) {
        return aggregateFunctionWithAlias(AggregateFunction.Type.COUNT, tableName, columnName, alias);
    }

    public static AggregateFunction count(String columnName) {
        return aggregateFunction(AggregateFunction.Type.COUNT, columnName);
    }

    public static AggregateFunction count(String tableName, String columnName) {
        return aggregateFunction(AggregateFunction.Type.COUNT, tableName, columnName);
    }

    public static AggregateFunction countWithAlias(ColumnRef columnRef, String alias) {
        return aggregateFunctionWithAlias(AggregateFunction.Type.COUNT, columnRef, alias);
    }

    public static AggregateFunction count(ColumnRef columnRef) {
        return aggregateFunction(AggregateFunction.Type.COUNT, columnRef);
    }

    public static AggregateFunction groupSumWithAlias(ColumnExpression columnExpression, String alias) {
        return aggregateFunctionWithAlias(AggregateFunction.Type.SUM, columnExpression, alias);
    }

    public static AggregateFunction groupSumWithAlias(String columnName, String alias) {
        return groupSumWithAlias(columnRef(columnName), alias);
    }

    public static AggregateFunction groupSumWithAlias(String tableName, String columnName, String alias) {
        return groupSumWithAlias(columnRef(tableName, columnName), alias);
    }

    public static AggregateFunction groupSum(ColumnExpression columnExpression) {
        return groupSumWithAlias(columnExpression, "");
    }

    public static AggregateFunction groupSum(String columnName) {
        return groupSum(columnRef(columnName));
    }

    public static AggregateFunction groupSum(String tableName, String columnName) {
        return groupSum(columnRef(tableName, columnName));
    }

    public static AggregateFunction groupAvgWithAlias(ColumnExpression columnExpression, String alias) {
        return aggregateFunctionWithAlias(AggregateFunction.Type.AVG, columnExpression, alias);
    }

    public static AggregateFunction groupAvgWithAlias(String columnName, String alias) {
        return groupAvgWithAlias(columnRef(columnName), alias);
    }

    public static AggregateFunction groupAvgWithAlias(String tableName, String columnName, String alias) {
        return groupAvgWithAlias(columnRef(tableName, columnName), alias);
    }

    public static AggregateFunction groupAvg(ColumnExpression columnExpression) {
        return groupAvgWithAlias(columnExpression, "");
    }

    public static AggregateFunction groupAvg(String columnName) {
        return groupAvg(columnRef(columnName));
    }

    public static AggregateFunction groupAvg(String tableName, String columnName) {
        return groupAvg(columnRef(tableName, columnName));
    }

    public static AggregateFunction groupMaxWithAlias(ColumnExpression columnExpression, String alias) {
        return aggregateFunctionWithAlias(AggregateFunction.Type.MAX, columnExpression, alias);
    }

    public static AggregateFunction groupMaxWithAlias(String columnName, String alias) {
        return groupMaxWithAlias(columnRef(columnName), alias);
    }

    public static AggregateFunction groupMaxWithAlias(String tableName, String columnName, String alias) {
        return groupMaxWithAlias(columnRef(tableName, columnName), alias);
    }

    public static AggregateFunction groupMax(ColumnExpression columnExpression) {
        return groupMaxWithAlias(columnExpression, "");
    }

    public static AggregateFunction groupMax(String columnName) {
        return groupMax(columnRef(columnName));
    }

    public static AggregateFunction groupMax(String tableName, String columnName) {
        return groupMax(columnRef(tableName, columnName));
    }

    public static AggregateFunction groupMinWithAlias(ColumnExpression columnExpression, String alias) {
        return aggregateFunctionWithAlias(AggregateFunction.Type.MIN, columnExpression, alias);
    }

    public static AggregateFunction groupMinWithAlias(String columnName, String alias) {
        return groupMinWithAlias(columnRef(columnName), alias);
    }

    public static AggregateFunction groupMinWithAlias(String tableName, String columnName, String alias) {
        return groupMinWithAlias(columnRef(tableName, columnName), alias);
    }

    public static AggregateFunction groupMin(ColumnExpression columnExpression) {
        return groupMinWithAlias(columnExpression, "");
    }

    public static AggregateFunction groupMin(String columnName) {
        return groupMin(columnRef(columnName));
    }

    public static AggregateFunction groupMin(String tableName, String columnName) {
        return groupMin(columnRef(tableName, columnName));
    }


    private static AggregateFunction aggregateFunctionWithAlias(AggregateFunction.Type type,
                                                                ColumnExpression columnExpr,
                                                                String alias) {
        return new AggregateFunctionImpl(type, columnExpr, alias);
    }

    private static AggregateFunction aggregateFunctionWithAlias(AggregateFunction.Type type,
                                                                String columnName,
                                                                String alias) {
        return new AggregateFunctionImpl(type, columnRef(columnName), alias);
    }

    private static AggregateFunction aggregateFunctionWithAlias(AggregateFunction.Type type,
                                                                String tableName,
                                                                String columnName,
                                                                String alias) {
        return new AggregateFunctionImpl(type, columnRef(tableName, columnName), alias);
    }

    private static AggregateFunction aggregateFunction(AggregateFunction.Type type,
                                                       ColumnExpression columnExpr) {
        return aggregateFunctionWithAlias(type, columnExpr, "");
    }

    private static AggregateFunction aggregateFunction(AggregateFunction.Type type,
                                                       String columnName) {
        return aggregateFunction(type, columnRef(columnName));
    }

    private static AggregateFunction aggregateFunction(AggregateFunction.Type type,
                                                       String tableName,
                                                       String columnName) {
        return aggregateFunction(type, columnRef(tableName, columnName));
    }


}
