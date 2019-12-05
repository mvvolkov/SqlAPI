package sqlapi.exceptions;

import org.jetbrains.annotations.NotNull;
import sqlapi.columnExpr.AggregateFunction;

public final class UnsupportedAggregateFunctionTypeException extends SqlException {

    @NotNull
    private final AggregateFunction aggregateFunction;


    public UnsupportedAggregateFunctionTypeException(@NotNull AggregateFunction aggregateFunction) {
        this.aggregateFunction = aggregateFunction;
    }

    @NotNull
    public AggregateFunction getAggregateFunction() {
        return aggregateFunction;
    }

    @Override
    public String getMessage() {
        return "Unsupported aggregate function type: " + aggregateFunction;
    }
}
