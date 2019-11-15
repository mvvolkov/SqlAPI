package api.exceptions;

public class AmbiguousColumnNameException extends SqlException {


    private final String columnName;


    public AmbiguousColumnNameException(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }

    @Override
    public String getMessage() {
        return "Ambiguous column name: " + columnName;
    }
}
