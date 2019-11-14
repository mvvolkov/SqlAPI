package serverLocalFileImpl;

import api.columnExpr.ColumnRef;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class ColumnRefImpl implements ColumnRef {

    @Nullable
    private final String schemaName;

    @Nullable
    private final String tableName;

    @NotNull
    private final String columnName;

    public ColumnRefImpl(@Nullable String schemaName, @Nullable String tableName,
                         @NotNull String columnName) {
        this.schemaName = schemaName;
        this.tableName = tableName;
        this.columnName = columnName;
    }

    public ColumnRefImpl(@Nullable String tableName,
                         @NotNull String columnName) {
        this(null, tableName, columnName);
    }

    public ColumnRefImpl(@NotNull String columnName) {
        this(null, null, columnName);
    }


    public ColumnRefImpl(@NotNull ColumnRef columnRef) {
        this.schemaName = columnRef.getSchemaName();
        this.tableName = columnRef.getTableName();
        this.columnName = columnRef.getColumnName();
    }

    @Override
    public String getColumnName() {
        return columnName;
    }

    @Override
    public String getTableName() {
        return tableName;
    }

    @Override
    public String getSchemaName() {
        return schemaName;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ColumnRefImpl)) {
            return false;
        }
        ColumnRefImpl o = (ColumnRefImpl) obj;
        return Objects.equals(schemaName, o.schemaName) &&
                Objects.equals(tableName, o.tableName)
                && Objects.equals(columnName, o.columnName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(schemaName, tableName, columnName);
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
