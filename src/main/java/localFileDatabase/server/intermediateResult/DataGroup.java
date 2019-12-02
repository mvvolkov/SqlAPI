package localFileDatabase.server.intermediateResult;

import sqlapi.columnExpr.*;
import sqlapi.exceptions.*;
import sqlapi.metadata.SqlType;

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

    public DataValue evaluateColumnExpr(ColumnExpression ce)
            throws SqlException {

        if (ce instanceof BinaryColumnExpression) {
            return evaluateBinaryColumnExpr((BinaryColumnExpression) ce);
        }
        if (ce instanceof ColumnRef) {
            return evaluateColumnRef((ColumnRef) ce);
        }
        if (ce instanceof ColumnValue) {
            return new DataValue(((ColumnValue) ce).getValue());
        }
        if (ce instanceof AggregateFunction) {
            return evaluateAggregateFunction((AggregateFunction) ce);
        }
        return null;
    }


    private DataValue evaluateBinaryColumnExpr(BinaryColumnExpression bce)
            throws SqlException {
        DataValue leftValue = this.evaluateColumnExpr(bce.getLeftOperand());
        DataValue rightValue = this.evaluateColumnExpr(bce.getRightOperand());

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


    private DataValue evaluateColumnRef(ColumnRef cr)
            throws SqlException {

        if (!groupedByColumns.contains(new DataHeader(cr))) {
            throw new InvalidQueryException("Column can not be used outside aggregate " +
                    "function: " + cr.getColumnName());
        }
        return rows.get(0).evaluateColumnExpr(cr);


    }

    private DataValue evaluateAggregateFunction(AggregateFunction af)
            throws SqlException {

        if (af instanceof CountAggregateFunction) {
            return this.getCount(af.getColumnRef());
        }
        if (af instanceof SumAggregateFunction) {
            return this.getSum(af.getColumnRef());
        }
        if (af instanceof MaxAggregateFunction) {
            return this.getMax(af.getColumnRef());
        }
        if (af instanceof MinAggregateFunction) {
            return this.getMin(af.getColumnRef());
        }
        if (af instanceof AvgAggregateFunction) {
            return this.getAvg(af.getColumnRef());
        }
        throw new UnsupportedAggregateFunctionTypeException(af);
    }

    private DataValue getCount(ColumnRef cr)
            throws NoSuchColumnException, AmbiguousColumnNameException {
        int count = 0;
        for (DataRow row : rows) {
            if (cr.getColumnName().isEmpty() ||
                    row.evaluateColumnRef(cr).getValue() != null) {
                count++;
            }
        }
        return new DataValue(count);
    }

    private Collection<DataValue> getValues(ColumnRef cr)
            throws NoSuchColumnException, AmbiguousColumnNameException,
            WrongValueTypeException {
        // result type
        SqlType type = null;
        Collection<DataValue> values = new ArrayList<>();
        for (DataRow row : rows) {
            DataValue value = row.evaluateColumnRef(cr);
            if (value.getValue() == null) {
                //"Null value can not be used in aggregate function except for COUNT(*)");
                throw new WrongValueTypeException();
            }
            values.add(value);
        }
        return values;
    }

    private DataValue getSum(ColumnRef cr)
            throws NoSuchColumnException, AmbiguousColumnNameException,
            WrongValueTypeException {

        Collection<DataValue> values = this.getValues(cr);
        if (values.isEmpty()) {
            return DataValue.nullValue();
        }
        DataValue sum = new DataValue(0);
        for (DataValue value : values) {
            sum = sum.add(value);
        }
        return sum;
    }

    private DataValue getAvg(ColumnRef cr)
            throws NoSuchColumnException, AmbiguousColumnNameException,
            WrongValueTypeException {

        Collection<DataValue> values = this.getValues(cr);
        if (values.isEmpty()) {
            return DataValue.nullValue();
        }
        DataValue sum = new DataValue(0);
        for (DataValue value : values) {
            sum = sum.add(value);
        }
        return sum.divide(new DataValue(values.size()));
    }

    private DataValue getMax(ColumnRef cr)
            throws NoSuchColumnException, AmbiguousColumnNameException,
            WrongValueTypeException {
        Collection<DataValue> values = this.getValues(cr);
        if (values.isEmpty()) {
            return DataValue.nullValue();
        }
        DataValue maxValue = null;

        for (DataValue value : values) {
            if (maxValue == null) {
                maxValue = value;
                continue;
            }
            if (value.getComparisonResult(maxValue.getValue()) > 0) {
                maxValue = value;
            }
        }
        return maxValue;
    }

    private DataValue getMin(ColumnRef cr)
            throws NoSuchColumnException, AmbiguousColumnNameException,
            WrongValueTypeException {
        Collection<DataValue> values = this.getValues(cr);
        if (values.isEmpty()) {
            return DataValue.nullValue();
        }
        DataValue minValue = null;
        for (DataValue value : values) {
            if (minValue == null) {
                minValue = value;
                continue;
            }
            if (value.getComparisonResult(minValue.getValue()) < 0) {
                minValue = value;
            }
        }
        return minValue;
    }

    public static Collection<DataGroup> getGroups(
            List<ColumnRef> groupByColumns,
            Collection<DataRow> rows)
            throws NoSuchColumnException, AmbiguousColumnNameException {
        Map<List<Object>, List<DataRow>> map = new HashMap<>();
        for (DataRow row : rows) {
            List<Object> values = new ArrayList<>();
            for (ColumnRef cr : groupByColumns) {
                values.add(row.evaluateColumnRef(cr).getValue());
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
