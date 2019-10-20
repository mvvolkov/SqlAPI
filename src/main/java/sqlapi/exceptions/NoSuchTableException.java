package sqlapi.exceptions;

public class NoSuchTableException extends SqlException {

    private final String tableName;

    public NoSuchTableException(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public String getMessage() {
        return "The table with the name " + tableName + " not found.";
    }

    public String getTableName() {
        return tableName;
    }
}
