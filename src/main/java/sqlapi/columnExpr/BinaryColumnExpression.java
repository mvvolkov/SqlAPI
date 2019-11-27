package sqlapi.columnExpr;

import org.jetbrains.annotations.NotNull;

public interface BinaryColumnExpression extends ColumnExpression {

    @NotNull ColumnExpression getLeftOperand();

    @NotNull ColumnExpression getRightOperand();
}
