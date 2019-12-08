package sqlapi.exceptions;

import org.jetbrains.annotations.NotNull;

public final class FailedDatabaseValidationException extends SqlException {

    @NotNull
    private final String message;

    public FailedDatabaseValidationException(@NotNull String message) {
        this.message = message;
    }

    @Override public String getMessage() {
        return "Failed validation: " + message;
    }
}
