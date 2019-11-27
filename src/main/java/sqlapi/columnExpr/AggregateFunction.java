package sqlapi.columnExpr;

import org.jetbrains.annotations.NotNull;

public interface AggregateFunction extends ColumnExpression {

    @NotNull ColumnRef getColumnRef();
}
