package clientImpl.columnExpr;

import api.columnExpr.ColumnRef;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ColumnRefImpl extends ColumnExprImpl implements ColumnRef {

    @Nullable
    private final String schemaName;

    @Nullable
    private final String tableName;

    @NotNull
    private final String columnName;


    public ColumnRefImpl(@Nullable String schemaName, @Nullable String tableName,
                         @NotNull String columnName,
                         @Nullable String alias) {
        super(ExprType.COLUMN_REF, alias);
        this.schemaName = schemaName;
        this.tableName = tableName;
        this.columnName = columnName;
    }

    public ColumnRefImpl(@Nullable String tableName,
                         @NotNull String columnName,
                         @Nullable String alias) {
        this(null, tableName, columnName, alias);
    }

    public ColumnRefImpl(@NotNull String columnName,
                         @Nullable String alias) {
        this(null, columnName, alias);
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

    @Nullable
    @Override
    public String getSchemaName() {
        return schemaName;
    }

    @Override public @Nullable String getAlias() {
        return alias != null ? alias : columnName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(columnName);
        if (tableName != null) {
            sb.insert(0, ".");
            sb.insert(0, tableName);
        }
        if (schemaName != null) {
            sb.insert(0, ".");
            sb.insert(0, schemaName);
        }
        return sb.toString();
    }
}
