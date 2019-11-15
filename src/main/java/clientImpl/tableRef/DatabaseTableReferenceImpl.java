package clientImpl.tableRef;

import api.tables.DatabaseTableReference;
import org.jetbrains.annotations.NotNull;

public final class DatabaseTableReferenceImpl implements DatabaseTableReference {

    @NotNull
    private final String schemaName;

    @NotNull
    private final String tableName;


    DatabaseTableReferenceImpl(@NotNull String schemaName, @NotNull String tableName) {
        this.tableName = tableName;
        this.schemaName = schemaName;
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

}
