package sqlapi.predicates;

import org.jetbrains.annotations.NotNull;
import sqlapi.columnExpr.ColumnExpression;

public interface BinaryPredicate extends Predicate {

    @NotNull ColumnExpression getLeftOperand();

    @NotNull ColumnExpression getRightOperand();
}
