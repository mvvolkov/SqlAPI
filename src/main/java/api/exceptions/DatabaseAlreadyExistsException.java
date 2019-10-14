package api.exceptions;

public class DatabaseAlreadyExistsException extends SqlException {

    private final String databaseName;

    public DatabaseAlreadyExistsException(String databaseName) {
        this.databaseName = databaseName;
    }


    @Override
    public String getMessage() {
        return "Can not create a new database. The database with the name " + databaseName + " already exists.";
    }

    public String getDatabaseName() {
        return databaseName;
    }
}
