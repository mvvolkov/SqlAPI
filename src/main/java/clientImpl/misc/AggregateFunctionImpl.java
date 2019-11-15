package clientImpl.misc;

import api.misc.AggregateFunction;

public class AggregateFunctionImpl implements AggregateFunction {

    private final String columnName;

    private final Type type;

    private final String alias;

    public AggregateFunctionImpl(String columnName,
                                 Type type, String alias) {
        this.columnName = columnName;
        this.type = type;
        this.alias = alias;
    }

    @Override public String getColumnName() {
        return columnName;
    }

    @Override public String getAlias() {
        return alias;
    }

    @Override public Type getType() {
        return type;
    }

    @Override public String toString() {
        StringBuilder sb = new StringBuilder();
        switch (type) {
            case COUNT:
                sb.append("COUNT");
                break;
            case SUM:
                sb.append("SUM");
                break;
            case AVG:
                sb.append("AVG");
                break;
            case MAX:
                sb.append("MAX");
                break;
            case MIN:
                sb.append("MIN");
                break;
        }
        sb.append("(");
        if (columnName.isEmpty()) {
            sb.append("*");
        } else {
            sb.append(columnName);
        }
        sb.append(")");
        if (!alias.isEmpty()) {
            sb.append(" AS ");
            sb.append(alias);
        }
        return sb.toString();
    }
}
