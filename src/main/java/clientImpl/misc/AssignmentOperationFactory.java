package clientImpl.misc;

import api.columnExpr.ColumnExpression;
import api.misc.AssignmentOperation;

public class AssignmentOperationFactory {

    private AssignmentOperationFactory() {
    }

    public static AssignmentOperation assign(String columnName, ColumnExpression columnExpression) {
        return new AssignmentOperationImpl(columnName, columnExpression);
    }
}
