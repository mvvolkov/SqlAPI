package clientImpl.tableRef;

import api.tables.DatabaseTableReference;
import org.jetbrains.annotations.NotNull;

public final class DatabaseTableReferenceImpl implements DatabaseTableReference {

    @NotNull
    private final String databaseName;


    @NotNull
    private final String tableName;


    DatabaseTableReferenceImpl(@NotNull String databaseName, @NotNull String tableName) {
        this.databaseName = databaseName;
        this.tableName = tableName;
    }


    @NotNull
    @Override
    public String getTableName() {
        return tableName;
    }

    @NotNull
    @Override
    public String getSchemaName() {
        return "";
    }

    @NotNull
    @Override
    public String getDatabaseName() {
        return databaseName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(databaseName);
        if (!this.getSchemaName().isEmpty()) {
            sb.append(".");
            sb.append(this.getSchemaName());
        }
        sb.append(".");
        sb.append(tableName);
        return sb.toString();
    }
}
