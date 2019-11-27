package clientImpl.columnExpr;

import sqlapi.columnExpr.AggregateFunction;
import sqlapi.columnExpr.ColumnExpression;
import sqlapi.columnExpr.ColumnRef;
import org.jetbrains.annotations.NotNull;

abstract class AggregateFunctionImpl extends ColumnExprImpl
        implements AggregateFunction {


    @NotNull
    private final ColumnRef column;

    @NotNull
    private final String alias;

    AggregateFunctionImpl(@NotNull ColumnExpression column,
                          @NotNull String alias) {
        super(alias);
        this.column = (ColumnRef) column;
        this.alias = alias;
    }

    @NotNull
    @Override
    public ColumnRef getColumnRef() {
        return column;
    }

    @NotNull
    @Override
    public String getAlias() {
        return alias;
    }

    protected abstract String getFunctionName();


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getFunctionName());
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
