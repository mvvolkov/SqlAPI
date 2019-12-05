package sqlapi.columnExpr;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface BinaryColumnExpression extends ColumnExpression {

    @NotNull ColumnExpression getLeftOperand();

    @NotNull ColumnExpression getRightOperand();

    @Override default void setParameters(List<Object> parameters) {
        getLeftOperand().setParameters(parameters);
        getRightOperand().setParameters(parameters);
    }
}
