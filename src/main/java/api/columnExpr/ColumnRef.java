package api.columnExpr;

import org.jetbrains.annotations.NotNull;

public interface ColumnRef extends ColumnExpression {

    String getColumnName();

    String getTableName();

    String getSchemaName();

    @NotNull
    @Override
    default String getAlias() {
        return getColumnName();
    }

    @Override
    default ExprType getExprType() {
        return ExprType.COLUMN_REF;
    }
}
