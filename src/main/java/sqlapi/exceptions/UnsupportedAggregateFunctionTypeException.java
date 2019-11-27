package sqlapi.exceptions;

public class UnsupportedAggregateFunctionTypeException extends SqlException {

    private final String className;


    public UnsupportedAggregateFunctionTypeException(String className) {
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
