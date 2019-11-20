package serverLocalFileImpl.intermediateresult;

import sqlapi.columnExpr.*;
import sqlapi.exceptions.AmbiguousColumnNameException;
import sqlapi.exceptions.InvalidQueryException;
import sqlapi.exceptions.NoSuchColumnException;

import java.util.ArrayList;
import java.util.List;

public class DataGroup {

    private final List<DataHeader> groupedByColumns;
    private final List<Object> keyValues;
    private final List<DataRow> rows;


    public DataGroup(List<ColumnRef> groupedByColumns,
                     List<Object> keyValues, List<DataRow> rows) {
        this.groupedByColumns = new ArrayList<>();
        for (ColumnRef cr : groupedByColumns) {
            this.groupedByColumns.add(new DataHeader(cr));
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

        if (!groupedByColumns.contains(new DataHeader(cr))) {
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
                return this.getCount(af.getColumnRef());
            case SUM:
                return this.getSum(af.getColumnRef());
            case MAX:
                return this.getMax(af.getColumnRef());
            case MIN:
                return this.getMin(af.getColumnRef());
        }
        throw new InvalidQueryException("");

    }

    private Integer getCount(ColumnRef cr)
            throws NoSuchColumnException, AmbiguousColumnNameException {
        int count = 0;
        for (DataRow row : rows) {
            if (cr.getColumnName().isEmpty() || row.evaluateColumnRef(cr) != null) {
                count++;
            }
        }
        return count;
    }

    private Integer getSum(ColumnRef cr)
            throws NoSuchColumnException, AmbiguousColumnNameException,
            InvalidQueryException {
        Integer sum = 0;
        for (DataRow row : rows) {
            Object value = row.evaluateColumnRef(cr);
            if (value == null) {
                throw new InvalidQueryException("Null value can not be used in aggregate function except for COUNT(*)");
            }
            if (!(value instanceof Integer)) {
                throw new InvalidQueryException("Wrong value type in aggregate function");
            }
            sum += (Integer) value;
        }
        return sum;
    }

    private Object getMax(ColumnRef cr)
            throws NoSuchColumnException, AmbiguousColumnNameException,
            InvalidQueryException {
        Comparable max = null;
        for (DataRow row : rows) {
            Object value = row.evaluateColumnRef(cr);
            if (!(value instanceof Comparable)) {
                throw new InvalidQueryException("");
            }
            Comparable cmpValue = (Comparable) value;
            if (max == null) {
                max = cmpValue;
                continue;
            }
            if (cmpValue.compareTo(max) > 0) {
                max = cmpValue;
            }
        }
        return max;
    }

    private Object getMin(ColumnRef cr)
            throws NoSuchColumnException, AmbiguousColumnNameException,
            InvalidQueryException {
        Comparable min = null;
        for (DataRow row : rows) {
            Object value = row.evaluateColumnRef(cr);
            if (!(value instanceof Comparable)) {
                throw new InvalidQueryException("");
            }
            Comparable cmpValue = (Comparable) value;
            if (min == null) {
                min = cmpValue;
                continue;
            }
            if (cmpValue.compareTo(min) < 0) {
                min = cmpValue;
            }
        }
        return min;
    }
}
