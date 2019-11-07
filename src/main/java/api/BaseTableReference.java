package api;

public interface BaseTableReference extends TableReference {

    String getTableName();

    String getDatabaseName();

    @Override
    default Type getType() {
        return Type.BASE_TABLE;
    }
}
