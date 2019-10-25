package sqlapi.exceptions;

import java.sql.SQLException;

public class NoSuchColumnException extends SQLException {

    private final String columnName;

    public NoSuchColumnException(String databaseName) {
        this.columnName = databaseName;
    }

    @Override
    public String getMessage() {
        return "The column with the name " + columnName + " not found.";
    }

    public String getColumnName() {
        return columnName;
    }
}
