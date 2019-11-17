package api.exceptions;

public class NoSuchDatabaseException extends SqlException {

    private final String databaseName;

    public NoSuchDatabaseException(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    @Override public String getMessage() {
        return "Database does not exist: " + databaseName;
    }
}
