package clientImpl.columnExpr;

import org.jetbrains.annotations.NotNull;
import sqlapi.columnExpr.ColumnExpression;
import sqlapi.columnExpr.MinAggregateFunction;

final class MinAggregateFunctionImpl extends AggregateFunctionImpl implements MinAggregateFunction {

    MinAggregateFunctionImpl(@NotNull ColumnExpression column, @NotNull String alias) {
        super(column, alias);
    }

    @Override
    protected String getFunctionName() {
        return "MIN";
    }
}
