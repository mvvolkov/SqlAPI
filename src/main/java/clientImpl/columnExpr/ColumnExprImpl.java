package clientImpl.columnExpr;

import sqlapi.columnExpr.ColumnExpression;
import org.jetbrains.annotations.NotNull;

abstract class ColumnExprImpl implements ColumnExpression {


    @NotNull
    final String alias;

    ColumnExprImpl(@NotNull String alias) {
        this.alias = alias;
    }

    @NotNull
    @Override
    public String getAlias() {
        return alias;
    }

    @Override
    public @NotNull ColumnExpression add(@NotNull ColumnExpression otherExpression, @NotNull String alias) {
        return new SumColumnExpressionImpl(this, otherExpression, alias);
    }

    @Override
    public @NotNull ColumnExpression add(@NotNull ColumnExpression otherExpression) {
        return this.add(otherExpression, "");
    }

    @Override
    public @NotNull ColumnExpression subtract(@NotNull ColumnExpression otherExpression, @NotNull String alias) {
        return new DiffColumnExpressionImpl(this, otherExpression, alias);
    }

    @Override
    public @NotNull ColumnExpression subtract(@NotNull ColumnExpression otherExpression) {
        return this.subtract(otherExpression, "");
    }

    @Override
    public @NotNull ColumnExpression multiply(@NotNull ColumnExpression otherExpression, @NotNull String alias) {
        return new ProductColumnExpressionImpl(this, otherExpression, alias);
    }

    @Override
    public @NotNull ColumnExpression multiply(@NotNull ColumnExpression otherExpression) {
        return multiply(otherExpression, "");
    }

    @Override
    public @NotNull ColumnExpression divide(@NotNull ColumnExpression otherExpression, @NotNull String alias) {
        return new DivisionColumnExpressionImpl(this, otherExpression, alias);
    }

    @Override
    public @NotNull ColumnExpression divide(@NotNull ColumnExpression otherExpression) {
        return divide(otherExpression, "");
    }
}
