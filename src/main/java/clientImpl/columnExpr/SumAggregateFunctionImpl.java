package clientImpl.columnExpr;

import org.jetbrains.annotations.NotNull;
import sqlapi.columnExpr.ColumnExpression;
import sqlapi.columnExpr.aggregate.SumAggregateFunction;

final class SumAggregateFunctionImpl extends AggregateFunctionImpl implements SumAggregateFunction {

    SumAggregateFunctionImpl(@NotNull ColumnExpression column, @NotNull String alias) {
        super(column, alias);
    }

    @Override
    protected String getFunctionName() {
        return "SUM";
    }
}
