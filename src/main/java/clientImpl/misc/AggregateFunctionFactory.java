package clientImpl.misc;

import api.misc.AggregateFunction;

public class AggregateFunctionFactory {

    private AggregateFunctionFactory() {
    }

    public static AggregateFunction count(String columnName, String alias) {
        return new AggregateFunctionImpl(columnName, AggregateFunction.Type.COUNT, alias);
    }

    public static AggregateFunction count(String columnName) {
        return new AggregateFunctionImpl(columnName, AggregateFunction.Type.COUNT, "");
    }

    public static AggregateFunction sum(String columnName, String alias) {
        return new AggregateFunctionImpl(columnName, AggregateFunction.Type.SUM, alias);
    }

    public static AggregateFunction sum(String columnName) {
        return new AggregateFunctionImpl(columnName, AggregateFunction.Type.SUM, "");
    }

    public static AggregateFunction avg(String columnName, String alias) {
        return new AggregateFunctionImpl(columnName, AggregateFunction.Type.AVG, alias);
    }

    public static AggregateFunction avg(String columnName) {
        return new AggregateFunctionImpl(columnName, AggregateFunction.Type.AVG, "");
    }

    public static AggregateFunction max(String columnName, String alias) {
        return new AggregateFunctionImpl(columnName, AggregateFunction.Type.MAX, alias);
    }

    public static AggregateFunction max(String columnName) {
        return new AggregateFunctionImpl(columnName, AggregateFunction.Type.MAX, "");
    }

    public static AggregateFunction min(String columnName, String alias) {
        return new AggregateFunctionImpl(columnName, AggregateFunction.Type.MIN, alias);
    }

    public static AggregateFunction min(String columnName) {
        return new AggregateFunctionImpl(columnName, AggregateFunction.Type.MIN, "");
    }
}
