package sqlapi;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ColumnReference {

    @NotNull
    private final String columnName;
    @Nullable
    private final String tableName;
    @Nullable
    private final String databaseName;

    public ColumnReference(String columnName, String tableName, String databaseName) {
        this.columnName = columnName;
        this.tableName = tableName;
        this.databaseName = databaseName;
    }

    public ColumnReference(String columnName, String tableName) {
        this.columnName = columnName;
        this.tableName = tableName;
        this.databaseName = null;
    }

    public ColumnReference(String columnName) {
        this.columnName = columnName;
        this.tableName = null;
        this.databaseName = null;
    }


    public String getColumnName() {
        return columnName;
    }

    public String getTableName() {
        return tableName;
    }

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
