package sqlapi.exceptions;

import org.jetbrains.annotations.NotNull;

public final class WrongValueTypeException extends SqlException {

    @NotNull
    private final String expectedClassName;

    @NotNull
    private final Object object;


    public WrongValueTypeException(@NotNull String expectedClassName, @NotNull Object object) {
        this.expectedClassName = expectedClassName;
        this.object = object;
    }

    @Override
    public String getMessage() {
        return "Object " + String.valueOf(object)
                + " can not be used to create object of type " + expectedClassName;
    }

    @NotNull
    public String getExpectedClassName() {
        return expectedClassName;
    }

    @NotNull
    public Object getObject() {
        return object;
    }
}
