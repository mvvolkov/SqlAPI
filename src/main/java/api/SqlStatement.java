package api;

public interface SqlStatement {

    enum Type {
        CREATE_TABLE,
        INSERT,
        DELETE,
        UPDATE,
        DROP_TABLE
    }

    Type getType();

    String getDatabaseName();

    String getTableName();
}
