package clientDefaultImpl;

import org.jetbrains.annotations.NotNull;
import api.BaseTableReference;

public final class BaseTableReferenceImpl implements BaseTableReference {

    @NotNull
    private final String tableName;

    @NotNull
    private final String dbName;


    BaseTableReferenceImpl(@NotNull String tableName, @NotNull String dbName) {
        this.tableName = tableName;
        this.dbName = dbName;
    }


    @NotNull
    @Override
    public String getTableName() {
        return tableName;
    }

    @NotNull
    @Override
    public String getDatabaseName() {
        return dbName;
    }

}
