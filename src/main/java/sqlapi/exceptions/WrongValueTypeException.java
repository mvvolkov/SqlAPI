package sqlapi.exceptions;

public final class WrongValueTypeException extends SqlException {


    public WrongValueTypeException() {
        int a = 1;
    }

    @Override
    public String getMessage() {
        return "message to be done";
    }
}
