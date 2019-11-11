package clientImpl.predicates;

import api.columnExpr.ColumnExpression;
import api.predicates.BinaryPredicate;
import org.jetbrains.annotations.NotNull;

public class BinaryPredicateImpl extends PredicateImpl implements BinaryPredicate {

    private final ColumnExpression leftOperand;

    private final ColumnExpression rightOperand;

    public BinaryPredicateImpl(@NotNull Type type, ColumnExpression leftOperand, ColumnExpression rightOperand) {
        super(type);
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    @Override
    public ColumnExpression getLeftOperand() {
        return leftOperand;
    }

    @Override
    public ColumnExpression getRightOperand() {
        return rightOperand;
    }
}
