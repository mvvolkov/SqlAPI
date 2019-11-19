package serverLocalFileImpl;

import api.columnExpr.ColumnRef;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class ColumnRefImpl implements ColumnRef {

    @NotNull
    private final String schemaName;

    @NotNull
    private final String tableName;

    @NotNull
    private final String columnName;


    public ColumnRefImpl(@NotNull String schemaName, @NotNull String tableName,
                         @NotNull String columnName) {
        this.schemaName = schemaName;
        this.tableName = tableName;
        this.columnName = columnName;
    }


    public ColumnRefImpl(@NotNull String tableName,
                         @NotNull String columnName) {
        this("", tableName, columnName);
    }

    public ColumnRefImpl(@NotNull String columnName) {
        this("", "", columnName);
    }


    public ColumnRefImpl(@NotNull ColumnRef columnRef) {
        this.schemaName = columnRef.getSchemaName();
        this.tableName = columnRef.getTableName();
        this.columnName = columnRef.getColumnName();
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
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof ColumnRefImpl)) {
            return false;
        }
        ColumnRefImpl cr = (ColumnRefImpl) obj;
        return Objects.equals(schemaName, cr.schemaName) &&
                Objects.equals(tableName, cr.tableName)
                && Objects.equals(columnName, cr.columnName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(schemaName, tableName, columnName);
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
