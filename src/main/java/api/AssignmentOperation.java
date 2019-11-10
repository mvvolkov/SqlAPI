package api;

import api.columnExpr.ColumnExpression;

public interface AssignmentOperation {

    String getColumnName();

    ColumnExpression getValue();
}
