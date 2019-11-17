package serverLocalFileImpl;

import api.columnExpr.*;
import api.exceptions.AmbiguousColumnNameException;
import api.exceptions.InvalidQueryException;
import api.exceptions.NoSuchColumnException;

import java.util.ArrayList;
import java.util.List;

public class InternalResultGroup {

    private final List<ColumnRef> groupedByColumns;
    private final List<Object> keyValues;
    private final List<InternalResultRow> rows;


    public InternalResultGroup(List<ColumnRef> groupedByColumns,
                               List<Object> keyValues, List<InternalResultRow> rows) {
        this.groupedByColumns = new ArrayList<>();
        for (ColumnRef cr : groupedByColumns) {
            this.groupedByColumns.add(new ColumnRefImpl(cr));
        }
        this.keyValues = keyValues;
        this.rows = rows;
    }

    public Object evaluateColumnExpr(ColumnExpression ce)
            throws NoSuchColumnException, AmbiguousColumnNameException,
            InvalidQueryException {

        if (ce instanceof BinaryColumnExpression) {
            return evaluateBinaryColumnExpr((BinaryColumnExpression) ce);
        }
        if (ce instanceof ColumnRef) {
            return evaluateColumnRef((ColumnRef) ce);
        }
        if (ce instanceof ColumnValue) {
            return ((ColumnValue) ce).getValue();
        }
        if (ce instanceof AggregateFunction) {
            return evaluateAggregateFunction((AggregateFunction) ce);
        }
        return null;
    }


    private Object evaluateBinaryColumnExpr(BinaryColumnExpression bce)
            throws NoSuchColumnException, AmbiguousColumnNameException,
            InvalidQueryException {
        Object leftValue = this.evaluateColumnExpr(bce.getLeftOperand());
        Object rightValue =
                this.evaluateColumnExpr(bce.getRightOperand());
        switch (bce.getExprType()) {
            case SUM:
                if (leftValue instanceof Integer && rightValue instanceof Integer) {
                    return (Integer) (((Integer) leftValue) + ((Integer) rightValue));
                }
                if (leftValue instanceof String && rightValue instanceof String) {
                    return (String) (((String) leftValue) + ((String) rightValue));
                }
            case DIFF:
                if (leftValue instanceof Integer && rightValue instanceof Integer) {
                    return (Integer) (((Integer) leftValue) - ((Integer) rightValue));
                }
            case PRODUCT:
                if (leftValue instanceof Integer && rightValue instanceof Integer) {
                    return (Integer) (((Integer) leftValue) * ((Integer) rightValue));
                }
            case DIVIDE:
                if (leftValue instanceof Integer && rightValue instanceof Integer) {
                    return (Integer) (((Integer) leftValue) / ((Integer) rightValue));
                }
        }
        return null;
    }


    private Object evaluateColumnRef(ColumnRef cr)
            throws InvalidQueryException, NoSuchColumnException,
            AmbiguousColumnNameException {

        if (!groupedByColumns.contains(new ColumnRefImpl(cr))) {
            throw new InvalidQueryException("Column can not be used outside aggregate " +
                    "function: " + cr.getColumnName());
        }
        return rows.get(0).evaluateColumnExpr(cr);


    }

    private Object evaluateAggregateFunction(AggregateFunction af)
            throws InvalidQueryException, NoSuchColumnException,
            AmbiguousColumnNameException {

        switch (af.getType()) {
            case COUNT:
                return this.getCount(af.getColumn());
        }
        throw new InvalidQueryException("");

    }

    private Integer getCount(ColumnRef cr)
            throws NoSuchColumnException, AmbiguousColumnNameException {
        int count = 0;
        for (InternalResultRow row : rows) {
            if (cr.getColumnName().isEmpty() || row.evaluateColumnRef(cr) != null) {
                count++;
            }
        }
        return count;
    }
}
