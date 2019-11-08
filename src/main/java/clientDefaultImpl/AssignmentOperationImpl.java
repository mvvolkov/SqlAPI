package clientDefaultImpl;

import api.AssignmentOperation;

public final class AssignmentOperationImpl implements AssignmentOperation {

    private final String columnName;
    private final Object value;

    public AssignmentOperationImpl(String columnName, Object value) {
        this.columnName = columnName;
        this.value = value;
    }


    @Override
    public String getColumnName() {
        return columnName;
    }

    @Override
    public Object getValue() {
        return value;
    }
}
