package sqlapi.exceptions;

public class FailedDatabaseValidationException extends SqlException {

    public String getDatabaseName() {
        return databaseName;
    }

    private final String databaseName;

    public FailedDatabaseValidationException(String databaseName) {
        this.databaseName = databaseName;
    }


    @Override public String getMessage() {
        return "Failed validation for database " + databaseName;
    }
}
