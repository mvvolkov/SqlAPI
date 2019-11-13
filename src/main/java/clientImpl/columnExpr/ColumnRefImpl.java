package clientImpl.columnExpr;

import api.columnExpr.ColumnExpression;
import api.columnExpr.ColumnRef;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ColumnRefImpl extends ColumnExprImpl implements ColumnRef {

    @NotNull
    private final String columnName;

    @NotNull
    private final String tableName;

    @NotNull
    private final String databaseName;

    public ColumnRefImpl(@NotNull String databaseName, @NotNull String tableName,
                         @NotNull String columnName,
                         @Nullable String alias) {
        super(ExprType.COLUMN_REF, alias);
        this.databaseName = databaseName;
        this.tableName = tableName;
        this.columnName = columnName;
    }

    @NotNull
    @Override
    public String getColumnName() {
        return columnName;
    }

    @Nullable
    @Override
    public String getTableName() {
        return tableName;
    }

    @NotNull
    @Override
    public String getDatabaseName() {
        return databaseName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(columnName);
        if (tableName != null) {
            sb.insert(0, ".");
            sb.insert(0, tableName);
        }
        if (databaseName != null) {
            sb.insert(0, ".");
            sb.insert(0, databaseName);
        }
        return sb.toString();
    }
}
