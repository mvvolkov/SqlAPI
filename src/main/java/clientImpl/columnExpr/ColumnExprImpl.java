package clientImpl.columnExpr;

import api.columnExpr.ColumnExpression;
import org.jetbrains.annotations.NotNull;

public abstract class ColumnExprImpl implements ColumnExpression {

    @NotNull
    protected final ExprType exprType;

    @NotNull
    protected final String alias;

    protected ColumnExprImpl(@NotNull ExprType exprType, @NotNull String alias) {
        this.exprType = exprType;
        this.alias = alias;
    }

    @NotNull
    @Override
    public String getAlias() {
        return alias;
    }

    @NotNull
    @Override
    public ExprType getExprType() {
        return exprType;
    }

}
