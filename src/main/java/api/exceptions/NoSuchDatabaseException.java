package api.exceptions;

public class NoSuchDatabaseException extends SqlException {

    private final String databaseName;

    public NoSuchDatabaseException(String databaseName) {
        this.databaseName = databaseName;
    }


    @Override
    public String getMessage() {
        return "The database with the name " + databaseName + " not found.";
    }

    public String getDatabaseName() {
        return databaseName;
    }
}
