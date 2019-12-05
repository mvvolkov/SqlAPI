package localFileDatabase.server.intermediate;

import org.jetbrains.annotations.NotNull;
import sqlapi.columnExpr.*;
import sqlapi.exceptions.SqlException;
import sqlapi.exceptions.UnsupportedColumnExprTypeException;

/**
 * Base class for {@link ResultRow} and {@link ResultGroup}.
 */
public abstract class AbstractResultRow {

    @NotNull
    public ResultValue evaluateColumnExpr(@NotNull ColumnExpression ce)
            throws SqlException {

        if (ce instanceof BinaryColumnExpression) {
            return this.evaluateBinaryColumnExpr((BinaryColumnExpression) ce);
        }
        if (ce instanceof ColumnRef) {
            return this.evaluateColumnRef((ColumnRef) ce);
        }
        if (ce instanceof ColumnValue) {
            return new ResultValue(((ColumnValue) ce).getValue());
        }
        if (ce instanceof AggregateFunction) {
            return evaluateAggregateFunction((AggregateFunction) ce);
        }
        throw new UnsupportedColumnExprTypeException(ce);
    }

    protected abstract ResultValue evaluateColumnRef(@NotNull ColumnRef cr)
            throws SqlException;

    protected abstract ResultValue evaluateAggregateFunction(@NotNull AggregateFunction af)
            throws SqlException;

    @NotNull
    private ResultValue evaluateBinaryColumnExpr(@NotNull BinaryColumnExpression bce)
            throws SqlException {
        ResultValue leftValue = this.evaluateColumnExpr(bce.getLeftOperand());
        ResultValue rightValue = this.evaluateColumnExpr(bce.getRightOperand());

        if (bce instanceof SumColumnExpression) {
            return leftValue.add(rightValue);
        }
        if (bce instanceof DiffColumnExpression) {
            return leftValue.subtract(rightValue);
        }
        if (bce instanceof ProductColumnExpression) {
            return leftValue.multiply(rightValue);
        }
        if (bce instanceof DivisionColumnExpression) {
            return leftValue.divide(rightValue);
        }
        throw new UnsupportedColumnExprTypeException(bce);
    }
}
