package clientImpl;

import api.AssignmentOperation;
import api.columnExpr.ColumnExpression;

public final class AssignmentOperationImpl implements AssignmentOperation {

    private final String columnName;
    private final ColumnExpression value;

    public AssignmentOperationImpl(String columnName, ColumnExpression value) {
        this.columnName = columnName;
        this.value = value;
    }


    @Override
    public String getColumnName() {
        return columnName;
    }

    @Override
    public ColumnExpression getValue() {
        return value;
    }
}
