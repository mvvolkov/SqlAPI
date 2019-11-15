package clientImpl.columnExpr;

import api.columnExpr.ColumnRef;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ColumnRefImpl extends ColumnExprImpl implements ColumnRef {

    @NotNull
    private final String schemaName;

    @NotNull
    private final String tableName;

    @NotNull
    private final String columnName;

    public ColumnRefImpl(@NotNull String schemaName, @NotNull String tableName,
                         @NotNull String columnName, String alias) {
        super(ExprType.COLUMN_REF, alias);
        this.schemaName = schemaName;
        this.tableName = tableName;
        this.columnName = columnName;
    }

    public ColumnRefImpl(@NotNull String tableName,
                         @NotNull String columnName, String alias) {
        this("", tableName, columnName, alias);
    }

    public ColumnRefImpl(@NotNull String columnName, String alias) {
        this("", "", columnName, alias);
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

    @NotNull
    @Override
    public String getSchemaName() {
        return schemaName;
    }

    @Override
    public @Nullable String getAlias() {
        return alias.isEmpty() ? columnName : alias;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(columnName);
        if (!tableName.isEmpty()) {
            sb.insert(0, ".");
            sb.insert(0, tableName);
        }
        if (!schemaName.isEmpty()) {
            sb.insert(0, ".");
            sb.insert(0, schemaName);
        }
        return sb.toString();
    }
}
