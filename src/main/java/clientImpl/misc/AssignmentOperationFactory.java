package clientImpl.misc;

import sqlapi.columnExpr.ColumnExpression;
import sqlapi.misc.AssignmentOperation;

public class AssignmentOperationFactory {

    private AssignmentOperationFactory() {
    }

    public static AssignmentOperation assign(String columnName, ColumnExpression columnExpression) {
        return new AssignmentOperationImpl(columnName, columnExpression);
    }
}
