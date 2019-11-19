package clientImpl.columnExpr;

import api.columnExpr.AggregateFunction;
import api.columnExpr.ColumnRef;
import org.jetbrains.annotations.NotNull;

public final class AggregateFunctionImpl extends ColumnExprImpl
        implements AggregateFunction {

    private final ColumnRef column;

    private final Type type;

    private final String alias;

    public AggregateFunctionImpl(ColumnRef column,
                                 Type type, String alias) {
        super(ExprType.AGGR_FUNC, alias);
        this.column = column;
        this.type = type;
        this.alias = alias;
    }

    @Override
    public ColumnRef getColumnRef() {
        return column;
    }

    @NotNull
    @Override
    public String getAlias() {
        return alias;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
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
        if (column.getColumnName().isEmpty()) {
            sb.append("*");
        } else {
            sb.append(column);
        }
        sb.append(")");
        if (!alias.isEmpty()) {
            sb.append(" AS ");
            sb.append(alias);
        }
        return sb.toString();
    }
}
