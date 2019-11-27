package clientImpl.columnExpr;

import org.jetbrains.annotations.NotNull;
import sqlapi.columnExpr.AvgAggregateFunction;
import sqlapi.columnExpr.ColumnExpression;

final class AvgAggregateFunctionImpl extends AggregateFunctionImpl implements AvgAggregateFunction {

    AvgAggregateFunctionImpl(@NotNull ColumnExpression column, @NotNull String alias) {
        super(column, alias);
    }

    @Override
    protected String getFunctionName() {
        return "AVG";
    }
}
