package sqlapi.columnExpr;

import org.jetbrains.annotations.NotNull;

public interface ColumnRef extends ColumnExpression {

    @NotNull String getColumnName();

    @NotNull String getTableName();

    @NotNull String getDatabaseName();

    @NotNull
    @Override
    default String getAlias() {
        return getColumnName();
    }

}
