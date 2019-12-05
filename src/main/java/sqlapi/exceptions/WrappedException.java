package sqlapi.exceptions;

import org.jetbrains.annotations.NotNull;

public final class WrappedException extends SqlException {

    @NotNull
    private final Exception exception;

    public WrappedException(@NotNull Exception exception) {
        this.exception = exception;
    }

    @NotNull
    public Exception getException() {
        return exception;
    }

    @Override
    public String getMessage() {
        return exception.getMessage();
    }
}
