package clientImpl.columnExpr;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sqlapi.columnExpr.*;

import java.util.ArrayList;
import java.util.List;

public class ColumnExprFactory {

    private ColumnExprFactory() {
    }

    // *************************** ColumnValue **********************************

    public static @NotNull ColumnValue valueWithAlias(@Nullable Object value, @NotNull String alias) {
        return new ColumnValueImpl(value, alias);
    }

    public static @NotNull ColumnValue valueWithAlias(@Nullable Object value) {
        return valueWithAlias(value, "");
    }

    public static @NotNull List<ColumnValue> values(Object... objects) {
        List<ColumnValue> columnValues = new ArrayList<>();
        for (Object obj : objects) {
            columnValues.add(valueWithAlias(obj));
        }
        return columnValues;
    }

    // *************************** ColumnRef **********************************

    public static @NotNull ColumnRef columnRefWithAlias(@NotNull String databaseName,
                                                        @NotNull String tableName,
                                                        @NotNull String columnName,
                                                        @NotNull String alias) {
        return new ColumnRefImpl(databaseName, tableName, columnName, alias);
    }

    public static @NotNull ColumnRef columnRefWithAlias(@NotNull String tableName,
                                                        String columnName,
                                                        @NotNull String alias) {
        return columnRefWithAlias("", tableName, columnName, alias);
    }

    public static @NotNull ColumnRef columnRefWithAlias(@NotNull String columnName,
                                                        @NotNull String alias) {
        return columnRefWithAlias("", columnName, alias);
    }

    public static @NotNull ColumnRef columnRef(@NotNull String databaseName,
                                               @NotNull String tableName,
                                               @NotNull String columnName) {
        return columnRefWithAlias(databaseName, tableName, columnName, "");
    }

    public static @NotNull ColumnRef columnRef(@NotNull String tableName,
                                               @NotNull String columnName) {
        return columnRef("", tableName, columnName);
    }

    public static @NotNull ColumnRef columnRef(@NotNull String columnName) {
        return columnRef("", columnName);
    }

    // *************************** SumColumnExpression **********************************

    public static @NotNull SumColumnExpression sumWithAlias(@NotNull ColumnExpression leftOperand,
                                                            @NotNull ColumnExpression rightOperand,
                                                            @NotNull String alias) {
        return new SumColumnExpressionImpl(leftOperand,
                rightOperand, alias);
    }

    public static @NotNull SumColumnExpression sumWithAlias(@NotNull ColumnExpression leftOperand,
                                                            @NotNull String columnName,
                                                            @NotNull String alias) {
        return sumWithAlias(leftOperand, columnRef(columnName), alias);
    }

    public static @NotNull SumColumnExpression sumWithAlias(@NotNull ColumnExpression leftOperand,
                                                            @NotNull String tableName,
                                                            @NotNull String columnName,
                                                            @NotNull String alias) {
        return sumWithAlias(leftOperand, columnRef(tableName, columnName), alias);
    }

    public static @NotNull SumColumnExpression sumWithAlias(@NotNull String columnName,
                                                            @NotNull ColumnExpression rightOperand,
                                                            @NotNull String alias) {
        return sumWithAlias(columnRef(columnName), rightOperand, alias);
    }

    public static @NotNull SumColumnExpression sumWithAlias(@NotNull String tableName, @NotNull String columnName,
                                                            @NotNull ColumnExpression rightOperand,
                                                            @NotNull String alias) {
        return sumWithAlias(columnRef(tableName, columnName), rightOperand, alias);
    }

    public static @NotNull SumColumnExpression sumWithAlias(@NotNull String columnName1,
                                                            @NotNull String columnName2,
                                                            @NotNull String alias) {
        return sumWithAlias(columnRef(columnName1), columnRef(columnName2), alias);
    }

    public static @NotNull SumColumnExpression sumWithAlias(@NotNull String tableName1, @NotNull String columnName1,
                                                            @NotNull String tableName2, @NotNull String columnName2,
                                                            @NotNull String alias) {
        return sumWithAlias(columnRef(tableName1, columnName1), columnRef(tableName2, columnName2), alias);
    }

    public static @NotNull SumColumnExpression sum(@NotNull ColumnExpression leftOperand,
                                                   @NotNull ColumnExpression rightOperand) {
        return sumWithAlias(leftOperand, rightOperand, "");
    }

    public static @NotNull SumColumnExpression sum(@NotNull ColumnExpression leftOperand,
                                                   @NotNull String columnName) {
        return sum(leftOperand, columnRef(columnName));
    }

    public static @NotNull SumColumnExpression sum(@NotNull ColumnExpression leftOperand,
                                                   @NotNull String tableName,
                                                   @NotNull String columnName) {
        return sum(leftOperand, columnRef(tableName, columnName));
    }

    public static @NotNull SumColumnExpression sum(@NotNull String columnName,
                                                   @NotNull ColumnExpression rightOperand) {
        return sum(columnRef(columnName), rightOperand);
    }

    public static @NotNull SumColumnExpression sum(@NotNull String tableName,
                                                   @NotNull String columnName,
                                                   @NotNull ColumnExpression rightOperand) {
        return sum(columnRef(tableName, columnName), rightOperand);
    }

    public static @NotNull SumColumnExpression sum(@NotNull String columnName1,
                                                   @NotNull String columnName2) {
        return sum(columnRef(columnName1), columnRef(columnName2));
    }

    public static @NotNull SumColumnExpression sum(@NotNull String tableName1,
                                                   @NotNull String columnName1,
                                                   @NotNull String tableName2,
                                                   @NotNull String columnName2) {
        return sum(columnRef(tableName1, columnName1), columnRef(tableName2, columnName2));
    }

    // *************************** DiffColumnExpression **********************************

    public static @NotNull DiffColumnExpression diffWithAlias(@NotNull ColumnExpression leftOperand,
                                                              @NotNull ColumnExpression rightOperand,
                                                              @NotNull String alias) {
        return new DiffColumnExpressionImpl(leftOperand, rightOperand, alias);
    }

    public static @NotNull DiffColumnExpression diffWithAlias(@NotNull ColumnExpression leftOperand,
                                                              @NotNull String columnName,
                                                              @NotNull String alias) {
        return diffWithAlias(leftOperand, columnRef(columnName), alias);
    }

    public static @NotNull DiffColumnExpression diffWithAlias(@NotNull ColumnExpression leftOperand,
                                                              @NotNull String tableName,
                                                              @NotNull String columnName,
                                                              @NotNull String alias) {
        return diffWithAlias(leftOperand, columnRef(tableName, columnName), alias);
    }

    public static @NotNull DiffColumnExpression diffWithAlias(@NotNull String columnName,
                                                              @NotNull ColumnExpression rightOperand,
                                                              @NotNull String alias) {
        return diffWithAlias(columnRef(columnName), rightOperand, alias);
    }

    public static @NotNull DiffColumnExpression diffWithAlias(@NotNull String tableName, @NotNull String columnName,
                                                              @NotNull ColumnExpression rightOperand,
                                                              @NotNull String alias) {
        return diffWithAlias(columnRef(tableName, columnName), rightOperand, alias);
    }

    public static @NotNull DiffColumnExpression diffWithAlias(@NotNull String columnName1,
                                                              @NotNull String columnName2,
                                                              @NotNull String alias) {
        return diffWithAlias(columnRef(columnName1), columnRef(columnName2), alias);
    }

    public static @NotNull DiffColumnExpression diffWithAlias(@NotNull String tableName1, @NotNull String columnName1,
                                                              @NotNull String tableName2, @NotNull String columnName2,
                                                              @NotNull String alias) {
        return diffWithAlias(columnRef(tableName1, columnName1), columnRef(tableName2, columnName2), alias);
    }

    public static @NotNull DiffColumnExpression diff(@NotNull ColumnExpression leftOperand,
                                                     @NotNull ColumnExpression rightOperand) {
        return diffWithAlias(leftOperand, rightOperand, "");
    }

    public static @NotNull DiffColumnExpression diff(@NotNull ColumnExpression leftOperand,
                                                     @NotNull String columnName) {
        return diff(leftOperand, columnRef(columnName));
    }

    public static @NotNull DiffColumnExpression diff(@NotNull ColumnExpression leftOperand,
                                                     @NotNull String tableName,
                                                     @NotNull String columnName) {
        return diff(leftOperand, columnRef(tableName, columnName));
    }

    public static @NotNull DiffColumnExpression diff(@NotNull String columnName,
                                                     @NotNull ColumnExpression rightOperand) {
        return diff(columnRef(columnName), rightOperand);
    }

    public static @NotNull DiffColumnExpression diff(@NotNull String tableName,
                                                     @NotNull String columnName,
                                                     @NotNull ColumnExpression rightOperand) {
        return diff(columnRef(tableName, columnName), rightOperand);
    }

    public static @NotNull DiffColumnExpression diff(@NotNull String columnName1,
                                                     @NotNull String columnName2) {
        return diff(columnRef(columnName1), columnRef(columnName2));
    }

    public static @NotNull DiffColumnExpression diff(@NotNull String tableName1,
                                                     @NotNull String columnName1,
                                                     @NotNull String tableName2,
                                                     @NotNull String columnName2) {
        return diff(columnRef(tableName1, columnName1), columnRef(tableName2, columnName2));
    }


    // *************************** ProductColumnExpression **********************************


    public static @NotNull ProductColumnExpression productWithAlias(@NotNull ColumnExpression leftOperand,
                                                                    @NotNull ColumnExpression rightOperand,
                                                                    @NotNull String alias) {
        return new ProductColumnExpressionImpl(leftOperand, rightOperand, alias);
    }

    public static @NotNull ProductColumnExpression productWithAlias(@NotNull ColumnExpression leftOperand,
                                                                    @NotNull String columnName,
                                                                    @NotNull String alias) {
        return productWithAlias(leftOperand, columnRef(columnName), alias);
    }

    public static @NotNull ProductColumnExpression productWithAlias(@NotNull ColumnExpression leftOperand,
                                                                    @NotNull String tableName,
                                                                    @NotNull String columnName,
                                                                    @NotNull String alias) {
        return productWithAlias(leftOperand, columnRef(tableName, columnName), alias);
    }

    public static @NotNull ProductColumnExpression productWithAlias(@NotNull String columnName,
                                                                    @NotNull ColumnExpression rightOperand,
                                                                    @NotNull String alias) {
        return productWithAlias(columnRef(columnName), rightOperand, alias);
    }

    public static @NotNull ProductColumnExpression productWithAlias(@NotNull String tableName, @NotNull String columnName,
                                                                    @NotNull ColumnExpression rightOperand,
                                                                    @NotNull String alias) {
        return productWithAlias(columnRef(tableName, columnName), rightOperand, alias);
    }

    public static @NotNull ProductColumnExpression productWithAlias(@NotNull String columnName1,
                                                                    @NotNull String columnName2,
                                                                    @NotNull String alias) {
        return productWithAlias(columnRef(columnName1), columnRef(columnName2), alias);
    }

    public static @NotNull ProductColumnExpression productWithAlias(@NotNull String tableName1, @NotNull String columnName1,
                                                                    @NotNull String tableName2, @NotNull String columnName2,
                                                                    @NotNull String alias) {
        return productWithAlias(columnRef(tableName1, columnName1), columnRef(tableName2, columnName2), alias);
    }

    public static @NotNull ProductColumnExpression product(@NotNull ColumnExpression leftOperand,
                                                           @NotNull ColumnExpression rightOperand) {
        return productWithAlias(leftOperand, rightOperand, "");
    }

    public static @NotNull ProductColumnExpression product(@NotNull ColumnExpression leftOperand,
                                                           @NotNull String columnName) {
        return product(leftOperand, columnRef(columnName));
    }

    public static @NotNull ProductColumnExpression product(@NotNull ColumnExpression leftOperand,
                                                           @NotNull String tableName,
                                                           @NotNull String columnName) {
        return product(leftOperand, columnRef(tableName, columnName));
    }

    public static @NotNull ProductColumnExpression product(@NotNull String columnName,
                                                           @NotNull ColumnExpression rightOperand) {
        return product(columnRef(columnName), rightOperand);
    }

    public static @NotNull ProductColumnExpression product(@NotNull String tableName,
                                                           @NotNull String columnName,
                                                           @NotNull ColumnExpression rightOperand) {
        return product(columnRef(tableName, columnName), rightOperand);
    }

    public static @NotNull ProductColumnExpression product(@NotNull String columnName1,
                                                           @NotNull String columnName2) {
        return product(columnRef(columnName1), columnRef(columnName2));
    }

    public static @NotNull ProductColumnExpression product(@NotNull String tableName1,
                                                           @NotNull String columnName1,
                                                           @NotNull String tableName2,
                                                           @NotNull String columnName2) {
        return product(columnRef(tableName1, columnName1), columnRef(tableName2, columnName2));
    }

    // *************************** DivisionColumnExpression **********************************


    public static @NotNull DivisionColumnExpression divisionWithAlias(@NotNull ColumnExpression leftOperand,
                                                                      @NotNull ColumnExpression rightOperand,
                                                                      @NotNull String alias) {
        return new DivisionColumnExpressionImpl(leftOperand, rightOperand, alias);
    }

    public static @NotNull DivisionColumnExpression divisionWithAlias(@NotNull ColumnExpression leftOperand,
                                                                      @NotNull String columnName,
                                                                      @NotNull String alias) {
        return divisionWithAlias(leftOperand, columnRef(columnName), alias);
    }

    public static @NotNull DivisionColumnExpression divisionWithAlias(@NotNull ColumnExpression leftOperand,
                                                                      @NotNull String tableName,
                                                                      @NotNull String columnName,
                                                                      @NotNull String alias) {
        return divisionWithAlias(leftOperand, columnRef(tableName, columnName), alias);
    }

    public static @NotNull DivisionColumnExpression divisionWithAlias(@NotNull String columnName,
                                                                      @NotNull ColumnExpression rightOperand,
                                                                      @NotNull String alias) {
        return divisionWithAlias(columnRef(columnName), rightOperand, alias);
    }

    public static @NotNull DivisionColumnExpression divisionWithAlias(@NotNull String tableName, @NotNull String columnName,
                                                                      @NotNull ColumnExpression rightOperand,
                                                                      @NotNull String alias) {
        return divisionWithAlias(columnRef(tableName, columnName), rightOperand, alias);
    }

    public static @NotNull DivisionColumnExpression divisionWithAlias(@NotNull String columnName1,
                                                                      @NotNull String columnName2,
                                                                      @NotNull String alias) {
        return divisionWithAlias(columnRef(columnName1), columnRef(columnName2), alias);
    }

    public static @NotNull DivisionColumnExpression divisionWithAlias(@NotNull String tableName1, @NotNull String columnName1,
                                                                      @NotNull String tableName2, @NotNull String columnName2,
                                                                      @NotNull String alias) {
        return divisionWithAlias(columnRef(tableName1, columnName1), columnRef(tableName2, columnName2), alias);
    }

    public static @NotNull DivisionColumnExpression division(@NotNull ColumnExpression leftOperand,
                                                             @NotNull ColumnExpression rightOperand) {
        return divisionWithAlias(leftOperand, rightOperand, "");
    }

    public static @NotNull DivisionColumnExpression division(@NotNull ColumnExpression leftOperand,
                                                             @NotNull String columnName) {
        return division(leftOperand, columnRef(columnName));
    }

    public static @NotNull DivisionColumnExpression division(@NotNull ColumnExpression leftOperand,
                                                             @NotNull String tableName,
                                                             @NotNull String columnName) {
        return division(leftOperand, columnRef(tableName, columnName));
    }

    public static @NotNull DivisionColumnExpression division(@NotNull String columnName,
                                                             @NotNull ColumnExpression rightOperand) {
        return division(columnRef(columnName), rightOperand);
    }

    public static @NotNull DivisionColumnExpression division(@NotNull String tableName,
                                                             @NotNull String columnName,
                                                             @NotNull ColumnExpression rightOperand) {
        return division(columnRef(tableName, columnName), rightOperand);
    }

    public static @NotNull DivisionColumnExpression division(@NotNull String columnName1,
                                                             @NotNull String columnName2) {
        return division(columnRef(columnName1), columnRef(columnName2));
    }

    public static @NotNull DivisionColumnExpression division(@NotNull String tableName1,
                                                             @NotNull String columnName1,
                                                             @NotNull String tableName2,
                                                             @NotNull String columnName2) {
        return division(columnRef(tableName1, columnName1), columnRef(tableName2, columnName2));
    }

    // *************************** CountAggregateFunction **********************************

    public static @NotNull CountAggregateFunction countWithAlias(@NotNull ColumnRef columnRef, @NotNull String alias) {
        return new CountAggregateFunctionImpl(columnRef, alias);
    }

    public static @NotNull CountAggregateFunction countWithAlias(@NotNull String tableName, @NotNull String columnName, @NotNull String alias) {
        return countWithAlias(columnRef(tableName, columnName), alias);
    }

    public static @NotNull CountAggregateFunction countWithAlias(@NotNull String columnName, @NotNull String alias) {
        return countWithAlias(columnRef(columnName), alias);
    }

    public static @NotNull CountAggregateFunction countAllWithAlias(@NotNull String alias) {
        return countWithAlias("", alias);
    }

    public static @NotNull CountAggregateFunction count(@NotNull ColumnRef columnRef) {
        return countWithAlias(columnRef, "");
    }

    public static @NotNull CountAggregateFunction count(@NotNull String tableName, @NotNull String columnName) {
        return count(columnRef(tableName, columnName));
    }

    public static @NotNull CountAggregateFunction count(@NotNull String columnName) {
        return count(columnRef(columnName));
    }

    public static @NotNull CountAggregateFunction countAll() {
        return count("");
    }

    // *************************** SumAggregateFunction **********************************

    public static @NotNull SumAggregateFunction groupSumWithAlias(@NotNull ColumnRef columnRef, @NotNull String alias) {
        return new SumAggregateFunctionImpl(columnRef, alias);
    }

    public static @NotNull SumAggregateFunction groupSumWithAlias(@NotNull String tableName, @NotNull String columnName, @NotNull String alias) {
        return groupSumWithAlias(columnRef(tableName, columnName), alias);
    }

    public static @NotNull SumAggregateFunction groupSumWithAlias(@NotNull String columnName, @NotNull String alias) {
        return groupSumWithAlias(columnRef(columnName), alias);
    }


    public static @NotNull SumAggregateFunction groupSum(@NotNull ColumnRef columnRef) {
        return groupSumWithAlias(columnRef, "");
    }

    public static @NotNull SumAggregateFunction groupSum(@NotNull String tableName, @NotNull String columnName) {
        return groupSum(columnRef(tableName, columnName));
    }

    public static @NotNull SumAggregateFunction groupSum(@NotNull String columnName) {
        return groupSum(columnRef(columnName));
    }

    // *************************** MaxAggregateFunction **********************************

    public static @NotNull MaxAggregateFunction maxWithAlias(@NotNull ColumnRef columnRef, @NotNull String alias) {
        return new MaxAggregateFunctionImpl(columnRef, alias);
    }

    public static @NotNull MaxAggregateFunction maxWithAlias(@NotNull String tableName, @NotNull String columnName, @NotNull String alias) {
        return maxWithAlias(columnRef(tableName, columnName), alias);
    }

    public static @NotNull MaxAggregateFunction maxWithAlias(@NotNull String columnName, @NotNull String alias) {
        return maxWithAlias(columnRef(columnName), alias);
    }


    public static @NotNull MaxAggregateFunction max(@NotNull ColumnRef columnRef) {
        return maxWithAlias(columnRef, "");
    }

    public static @NotNull MaxAggregateFunction max(@NotNull String tableName, @NotNull String columnName) {
        return max(columnRef(tableName, columnName));
    }

    public static @NotNull MaxAggregateFunction max(@NotNull String columnName) {
        return max(columnRef(columnName));
    }

    // *************************** MinAggregateFunction **********************************

    public static @NotNull MinAggregateFunction minWithAlias(@NotNull ColumnRef columnRef, @NotNull String alias) {
        return new MinAggregateFunctionImpl(columnRef, alias);
    }

    public static @NotNull MinAggregateFunction minWithAlias(@NotNull String tableName, @NotNull String columnName, @NotNull String alias) {
        return minWithAlias(columnRef(tableName, columnName), alias);
    }

    public static @NotNull MinAggregateFunction minWithAlias(@NotNull String columnName, @NotNull String alias) {
        return minWithAlias(columnRef(columnName), alias);
    }


    public static @NotNull MinAggregateFunction min(@NotNull ColumnRef columnRef) {
        return minWithAlias(columnRef, "");
    }

    public static @NotNull MinAggregateFunction min(@NotNull String tableName, @NotNull String columnName) {
        return min(columnRef(tableName, columnName));
    }

    public static @NotNull MinAggregateFunction min(@NotNull String columnName) {
        return min(columnRef(columnName));
    }

    // *************************** AvgAggregateFunction **********************************

    public static @NotNull AvgAggregateFunction avgWithAlias(@NotNull ColumnRef columnRef, @NotNull String alias) {
        return new AvgAggregateFunctionImpl(columnRef, alias);
    }

    public static @NotNull AvgAggregateFunction avgWithAlias(@NotNull String tableName, @NotNull String columnName, @NotNull String alias) {
        return avgWithAlias(columnRef(tableName, columnName), alias);
    }

    public static @NotNull AvgAggregateFunction avgWithAlias(@NotNull String columnName, @NotNull String alias) {
        return avgWithAlias(columnRef(columnName), alias);
    }


    public static @NotNull AvgAggregateFunction avg(@NotNull ColumnRef columnRef) {
        return avgWithAlias(columnRef, "");
    }

    public static @NotNull AvgAggregateFunction avg(@NotNull String tableName, @NotNull String columnName) {
        return avg(columnRef(tableName, columnName));
    }

    public static @NotNull AvgAggregateFunction avg(@NotNull String columnName) {
        return avg(columnRef(columnName));
    }


}
