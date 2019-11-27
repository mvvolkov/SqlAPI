package sqlapi.exceptions;

public class UnsupportedTableReferenceTypeException extends SqlException {

    private final String className;


    public UnsupportedTableReferenceTypeException(String className) {
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
