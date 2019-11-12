package api.exceptions;

public class InconsistentInsertStmtException extends SqlException {

    @Override
    public String getMessage() {
        return "The INSERT query is not consistent";
    }
}
