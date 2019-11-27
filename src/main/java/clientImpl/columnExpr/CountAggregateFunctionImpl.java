package clientImpl.columnExpr;

import org.jetbrains.annotations.NotNull;
import sqlapi.columnExpr.ColumnExpression;
import sqlapi.columnExpr.CountAggregateFunction;

final class CountAggregateFunctionImpl extends AggregateFunctionImpl implements CountAggregateFunction {

    CountAggregateFunctionImpl(@NotNull ColumnExpression column, @NotNull String alias) {
        super(column, alias);
    }

    @Override
    protected String getFunctionName() {
        return "COUNT";
    }
}
