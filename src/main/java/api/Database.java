package api;

public interface Database {

    String getName();

    SqlTable getTableOrNull(String tableName);

    void createTable(SqlTableDescription td);

    void dropTable(String tableName);
}
