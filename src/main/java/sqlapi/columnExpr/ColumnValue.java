package sqlapi.columnExpr;


import org.jetbrains.annotations.Nullable;

public interface ColumnValue extends ColumnExpression {

    @Nullable
    Object getValue();

}
