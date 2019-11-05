package api;

public interface BaseTableReference extends TableReference {

    String getTableName();

    String getDatabaseName();

    @Override
    default boolean isBaseTable() {
        return true;
    }
}
