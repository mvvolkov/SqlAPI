package sqlapi.exceptions;

public class MissingParameterException extends SqlException {

    @Override public String getMessage() {
        return "One of the parameters in a parametrized query is not set.";
    }
}
