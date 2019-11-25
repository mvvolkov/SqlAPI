package serverLocalFileImpl.intermediateResult;

import sqlapi.columnExpr.*;
import sqlapi.exceptions.AmbiguousColumnNameException;
import sqlapi.exceptions.InvalidQueryException;
import sqlapi.exceptions.NoSuchColumnException;

import java.util.*;

public final class DataGroup {

    private final List<DataHeader> groupedByColumns;
    private final List<DataRow> rows;


    public DataGroup(List<ColumnRef> groupedByColumns, List<DataRow> rows) {
        this.groupedByColumns = new ArrayList<>();
        for (ColumnRef cr : groupedByColumns) {
            this.groupedByColumns.add(new DataHeader(cr));
        }
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
        return DataRow.evaluateBinaryColumnExpr(leftValue, rightValue, bce.getExprType());
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
        throw new InvalidQueryException("Unknown type of aggregate function.");

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
                throw new InvalidQueryException(
                        "Null value can not be used in aggregate function except for COUNT(*)");
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
            if (value == null) {
                throw new InvalidQueryException(
                        "Null value can not be used in aggregate function except for COUNT(*)");
            }
            if (!(value instanceof Comparable)) {
                throw new InvalidQueryException(
                        "Comparable values only can be used for MAX()");
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
            if (value == null) {
                throw new InvalidQueryException(
                        "Null value can not be used in aggregate function except for COUNT(*)");
            }
            if (!(value instanceof Comparable)) {
                throw new InvalidQueryException(
                        "Comparable values only can be used for MIN()");
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

    public static Collection<DataGroup> getGroups(
            List<ColumnRef> groupByColumns,
            Collection<DataRow> rows)
            throws NoSuchColumnException, AmbiguousColumnNameException {
        Map<List<Object>, List<DataRow>> map = new HashMap<>();
        for (DataRow row : rows) {
            List<Object> values = new ArrayList<>();
            for (ColumnRef cr : groupByColumns) {
                values.add(row.evaluateColumnRef(cr));
            }
            List<Object> key = Collections.unmodifiableList(values);
            if (!map.containsKey(key)) {
                map.put(key, new ArrayList<>());
            }
            map.get(key).add(row);
        }
        Collection<DataGroup> groups = new ArrayList<>();
        for (List<DataRow> group : map.values()) {
            groups.add(new DataGroup(groupByColumns, group));
        }
        return groups;
    }
}
