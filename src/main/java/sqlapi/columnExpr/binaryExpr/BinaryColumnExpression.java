package sqlapi.columnExpr.binaryExpr;

import org.jetbrains.annotations.NotNull;
import sqlapi.columnExpr.ColumnExpression;

import java.util.List;

public interface BinaryColumnExpression extends ColumnExpression {

    @NotNull ColumnExpression getLeftOperand();

    @NotNull ColumnExpression getRightOperand();

    @Override default void setParameters(List<Object> parameters) {
        getLeftOperand().setParameters(parameters);
        getRightOperand().setParameters(parameters);
    }
}
