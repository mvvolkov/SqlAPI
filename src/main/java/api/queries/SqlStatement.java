package api.queries;

public interface SqlStatement {

    enum Type {
        CREATE_TABLE,
        INSERT,
        INSERT_FROM_SELECT,
        DELETE,
        UPDATE,
        DROP_TABLE
    }

    Type getType();

    String getDatabaseName();

    String getTableName();
}
