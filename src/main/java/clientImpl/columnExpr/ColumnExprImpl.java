package clientImpl.columnExpr;

import api.columnExpr.ColumnExpression;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ColumnExprImpl implements ColumnExpression {

    @NotNull
    protected final ExprType exprType;

    @Nullable
    protected final String alias;

    protected ColumnExprImpl(@NotNull ExprType exprType, @Nullable String alias) {
        this.exprType = exprType;
        this.alias = alias;
    }

    @Nullable
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
