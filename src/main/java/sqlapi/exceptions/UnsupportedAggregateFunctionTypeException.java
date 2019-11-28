package sqlapi.exceptions;

import sqlapi.columnExpr.AggregateFunction;

public class UnsupportedAggregateFunctionTypeException extends SqlException {

    private final AggregateFunction aggregateFunction;


    public UnsupportedAggregateFunctionTypeException(AggregateFunction aggregateFunction) {
        this.aggregateFunction = aggregateFunction;
    }

    public AggregateFunction getAggregateFunction() {
        return aggregateFunction;
    }

    @Override
    public String getMessage() {
        return "Unsupported aggregate function type: " + aggregateFunction;
    }
}
