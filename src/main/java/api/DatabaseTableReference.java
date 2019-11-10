package api;

public interface DatabaseTableReference extends TableReference {

    String getDatabaseName();

    String getTableName();

    @Override
    default Type getType() {
        return Type.DATABASE_TABLE;
    }
}
