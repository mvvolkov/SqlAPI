package sqlapi;

public class AssignmentOperation {

    private final String columnName;
    private final Object value;

    public AssignmentOperation(String columnName, Object value) {
        this.columnName = columnName;
        this.value = value;
    }

    @Override
    public String toString() {
        return columnName + " = " + value.toString();
    }
}
