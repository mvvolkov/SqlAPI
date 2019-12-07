package clientImpl.columnExpr;

import org.jetbrains.annotations.NotNull;
import sqlapi.columnExpr.ColumnExpression;
import sqlapi.columnExpr.aggregate.MaxAggregateFunction;

final class MaxAggregateFunctionImpl extends AggregateFunctionImpl implements MaxAggregateFunction {

    MaxAggregateFunctionImpl(@NotNull ColumnExpression column, @NotNull String alias) {
        super(column, alias);
    }

    @Override
    protected String getFunctionName() {
        return "MAX";
    }
}
