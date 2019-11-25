package clientImpl.columnExpr;

import sqlapi.columnExpr.ColumnExpression;
import org.jetbrains.annotations.NotNull;

abstract class ColumnExprImpl implements ColumnExpression {


    @NotNull final ExprType exprType;

    @NotNull final String alias;

    ColumnExprImpl(@NotNull ExprType exprType, @NotNull String alias) {
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
