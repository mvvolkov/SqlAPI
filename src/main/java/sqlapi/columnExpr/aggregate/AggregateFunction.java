package sqlapi.columnExpr.aggregate;

import org.jetbrains.annotations.NotNull;
import sqlapi.columnExpr.ColumnExpression;
import sqlapi.columnExpr.ColumnRef;

public interface AggregateFunction extends ColumnExpression {

    @NotNull ColumnRef getColumnRef();
}
