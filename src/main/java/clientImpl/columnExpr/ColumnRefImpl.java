package clientImpl.columnExpr;

import sqlapi.columnExpr.ColumnRef;
import org.jetbrains.annotations.NotNull;

final class ColumnRefImpl extends ColumnExprImpl implements ColumnRef {

    @NotNull
    private final String databaseName;

    @NotNull
    private final String tableName;

    @NotNull
    private final String columnName;

    ColumnRefImpl(@NotNull String databaseName, @NotNull String tableName,
                  @NotNull String columnName, String alias) {
        super(alias);
        this.databaseName = databaseName;
        this.tableName = tableName;
        this.columnName = columnName;
    }


    @NotNull
    @Override
    public String getColumnName() {
        return columnName;
    }

    @NotNull
    @Override
    public String getTableName() {
        return tableName;
    }

    @Override
    public @NotNull String getDatabaseName() {
        return databaseName;
    }


    @NotNull
    @Override
    public String getAlias() {
        return alias.isEmpty() ? columnName : alias;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(columnName);
        if (!tableName.isEmpty()) {
            sb.insert(0, ".");
            sb.insert(0, tableName);
        }
        if (!databaseName.isEmpty()) {
            sb.insert(0, ".");
            sb.insert(0, databaseName);
        }
        return sb.toString();
    }
}
