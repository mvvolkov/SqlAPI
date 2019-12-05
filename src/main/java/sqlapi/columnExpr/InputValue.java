package sqlapi.columnExpr;


import org.jetbrains.annotations.Nullable;

public interface InputValue extends ColumnExpression {

    @Nullable
    Object getValue();
}
