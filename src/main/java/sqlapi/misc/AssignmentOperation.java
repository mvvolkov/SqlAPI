package sqlapi.misc;

import sqlapi.columnExpr.ColumnExpression;

public interface AssignmentOperation {

    String getColumnName();

    ColumnExpression getValue();
}
