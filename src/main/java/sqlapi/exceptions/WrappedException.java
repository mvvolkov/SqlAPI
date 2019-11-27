package sqlapi.exceptions;

public class WrappedException extends SqlException {

    private final Exception exception;

    public WrappedException(Exception exception) {
        this.exception = exception;
    }

    public Exception getException() {
        return exception;
    }

    @Override
    public String getMessage() {
        return exception.getMessage();
    }
}
