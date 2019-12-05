package sqlapi.predicates;

import org.jetbrains.annotations.NotNull;
import sqlapi.columnExpr.ColumnExpression;

import java.util.List;

public interface BinaryPredicate extends Predicate {

    @NotNull ColumnExpression getLeftOperand();

    @NotNull ColumnExpression getRightOperand();

    @Override default void setParameters(List<Object> parameters) {
        getLeftOperand().setParameters(parameters);
        getRightOperand().setParameters(parameters);
    }
}
