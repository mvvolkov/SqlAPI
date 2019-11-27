package sqlapi.exceptions;

public class UnsupportedPredicateTypeException extends SqlException {

    private final String className;


    public UnsupportedPredicateTypeException(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    @Override
    public String getMessage() {
        return "Unsupported predicate type: " + className;
    }
}
