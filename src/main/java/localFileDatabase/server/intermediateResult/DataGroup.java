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

    public SqlValue evaluateColumnExpr(ColumnExpression ce)
            throws SqlException {

        if (ce instanceof BinaryColumnExpression) {
            return evaluateBinaryColumnExpr((BinaryColumnExpression) ce);
        }
        if (ce instanceof ColumnRef) {
            return evaluateColumnRef((ColumnRef) ce);
        }
        if (ce instanceof ColumnValue) {
            Object value = ((ColumnValue) ce).getValue();
            return new SqlValue(SqlValue.getSqlType(value), value);
        }
        if (ce instanceof AggregateFunction) {
            return evaluateAggregateFunction((AggregateFunction) ce);
        }
        return null;
    }


    private SqlValue evaluateBinaryColumnExpr(BinaryColumnExpression bce)
            throws SqlException {
        SqlValue leftValue = this.evaluateColumnExpr(bce.getLeftOperand());
        SqlValue rightValue = this.evaluateColumnExpr(bce.getRightOperand());

        if (bce instanceof SumColumnExpression) {
            return DataRow.evaluateSumColumnExpr(leftValue, rightValue);
        }
        if (bce instanceof DiffColumnExpression) {
            return DataRow.evaluateDiffColumnExpr(leftValue, rightValue);
        }
        if (bce instanceof ProductColumnExpression) {
            return DataRow.evaluateProductColumnExpr(leftValue, rightValue);
        }
        if (bce instanceof DivisionColumnExpression) {
            return DataRow.evaluateDivisionColumnExpr(leftValue, rightValue);
        }
        throw new UnsupportedColumnExprTypeException(bce);
    }


    private SqlValue evaluateColumnRef(ColumnRef cr)
            throws SqlException {

        if (!groupedByColumns.contains(new DataHeader(cr))) {
            throw new InvalidQueryException("Column can not be used outside aggregate " +
                    "function: " + cr.getColumnName());
        }
        return rows.get(0).evaluateColumnExpr(cr);


    }

    private SqlValue evaluateAggregateFunction(AggregateFunction af)
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

    private SqlValue getCount(ColumnRef cr)
            throws NoSuchColumnException, AmbiguousColumnNameException {
        int count = 0;
        for (DataRow row : rows) {
            if (cr.getColumnName().isEmpty() ||
                    row.evaluateColumnRef(cr).getValue() != null) {
                count++;
            }
        }
        return new SqlValue(SqlType.INTEGER, count);
    }

    private Collection<SqlValue> getValues(ColumnRef cr)
            throws NoSuchColumnException, AmbiguousColumnNameException,
            WrongValueTypeException {
        // result type
        SqlType type = null;
        Collection<SqlValue> values = new ArrayList<>();
        for (DataRow row : rows) {
            SqlValue value = row.evaluateColumnRef(cr);
            if (value.getValue() == null || value.getSqlType() == null) {
                //"Null value can not be used in aggregate function except for COUNT(*)");
                throw new WrongValueTypeException();
            }
            if (type == null) {
                type = value.getSqlType();
            } else {
                if (type != value.getSqlType()) {
                    throw new WrongValueTypeException();
                }
            }
            values.add(value);
        }
        return values;
    }

    private SqlValue getSum(ColumnRef cr)
            throws NoSuchColumnException, AmbiguousColumnNameException,
            WrongValueTypeException {

        Collection<SqlValue> values = this.getValues(cr);
        if (values.isEmpty()) {
            return new SqlValue(null, null);
        }
        SqlType sqlType = values.iterator().next().getSqlType();
        if (sqlType == SqlType.INTEGER) {
            int sum = 0;
            for (SqlValue value : values) {
                sum += (int) value.getValue();
            }
            return new SqlValue(sqlType, sum);
        }
        throw new WrongValueTypeException();
    }

    private SqlValue getAvg(ColumnRef cr)
            throws NoSuchColumnException, AmbiguousColumnNameException,
            WrongValueTypeException {

        Collection<SqlValue> values = this.getValues(cr);
        if (values.isEmpty()) {
            return new SqlValue(null, null);
        }
        SqlType sqlType = values.iterator().next().getSqlType();
        if (sqlType == SqlType.INTEGER) {
            int sum = 0;
            for (SqlValue value : values) {
                sum += (int) value.getValue();
            }
            return new SqlValue(sqlType, sum / values.size());
        }
        throw new WrongValueTypeException();
    }

    private SqlValue getMax(ColumnRef cr)
            throws NoSuchColumnException, AmbiguousColumnNameException,
            WrongValueTypeException {
        Collection<SqlValue> values = this.getValues(cr);
        if (values.isEmpty()) {
            return new SqlValue(null, null);
        }
        SqlType sqlType = values.iterator().next().getSqlType();
        if (sqlType == SqlType.INTEGER) {
            Integer maxValue = null;
            for (SqlValue sqlValue : values) {
                Integer value = (Integer) sqlValue.getValue();
                if (maxValue == null) {
                    maxValue = value;
                    continue;
                }
                if (value.compareTo(maxValue) > 0) {
                    maxValue = value;
                }
            }
            return new SqlValue(sqlType, maxValue);
        }
        if (sqlType == SqlType.VARCHAR) {
            String maxValue = null;
            for (SqlValue sqlValue : values) {
                String value = (String) sqlValue.getValue();
                if (maxValue == null) {
                    maxValue = value;
                    continue;
                }
                if (value.compareTo(maxValue) > 0) {
                    maxValue = value;
                }
            }
            return new SqlValue(sqlType, maxValue);
        }
        throw new WrongValueTypeException();
    }

    private SqlValue getMin(ColumnRef cr)
            throws NoSuchColumnException, AmbiguousColumnNameException,
            WrongValueTypeException {
        Collection<SqlValue> values = this.getValues(cr);
        if (values.isEmpty()) {
            return new SqlValue(null, null);
        }
        SqlType sqlType = values.iterator().next().getSqlType();
        if (sqlType == SqlType.INTEGER) {
            Integer minValue = null;
            for (SqlValue sqlValue : values) {
                Integer value = (Integer) sqlValue.getValue();
                if (minValue == null) {
                    minValue = value;
                    continue;
                }
                if (value < minValue) {
                    minValue = value;
                }
            }
            return new SqlValue(sqlType, minValue);
        }
        if (sqlType == SqlType.VARCHAR) {
            String minValue = null;
            for (SqlValue sqlValue : values) {
                String value = (String) sqlValue.getValue();
                if (minValue == null) {
                    minValue = value;
                    continue;
                }
                if (value.compareTo(minValue) < 0) {
                    minValue = value;
                }
            }
            return new SqlValue(sqlType, minValue);
        }
        throw new WrongValueTypeException();
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
