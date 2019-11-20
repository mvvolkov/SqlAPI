package clientImpl.columnExpr;

import sqlapi.columnExpr.AggregateFunction;
import sqlapi.columnExpr.ColumnExpression;
import sqlapi.columnExpr.ColumnRef;
import org.jetbrains.annotations.NotNull;

public final class AggregateFunctionImpl extends ColumnExprImpl
        implements AggregateFunction {

    private final Type type;

    private final ColumnRef column;

    private final String alias;

    public AggregateFunctionImpl(Type type, ColumnExpression column, String alias) {
        super(ExprType.AGGR_FUNC, alias);
        this.type = type;
        this.column = (ColumnRef) column;
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
