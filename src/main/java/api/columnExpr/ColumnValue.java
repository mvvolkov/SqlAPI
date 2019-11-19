package api.columnExpr;


import org.jetbrains.annotations.Nullable;

public interface ColumnValue extends ColumnExpression {

    @Nullable
    Object getValue();

    @Override
    default ExprType getExprType() {
        return ExprType.COLUMN_VALUE;
    }
}
