package api.exceptions;

public class InvalidQueryException extends SqlException {

    private final String message;


    public InvalidQueryException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
