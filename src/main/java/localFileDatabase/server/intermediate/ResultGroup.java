package localFileDatabase.server.intermediate;

import localFileDatabase.server.LocalFileDbServer;
import sqlapi.columnExpr.*;
import sqlapi.exceptions.*;
import sqlapi.misc.SelectedItem;
import sqlapi.tables.DatabaseTableReference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

final class ResultGroup {

    private LocalFileDbServer server;

    private final Collection<ResultHeader> groupedByColumns;

    private final Collection<ResultRow> rows;


    ResultGroup(LocalFileDbServer server, Collection<ResultHeader> groupedByColumns, Collection<ResultRow> rows) {
        this.server = server;
        this.groupedByColumns = groupedByColumns;
        this.rows = rows;
    }


    ResultRow getSelectedValues(List<SelectedItem> selectedItems, List<ResultHeader> headers) throws SqlException {

        List<ResultValue> newValues = new ArrayList<>();
        if (selectedItems.isEmpty()) {
            for (ResultHeader header : headers) {
                newValues.add(this.evaluateHeaderValue(header));
            }
        }
        for (SelectedItem selectedItem : selectedItems) {
            if (selectedItem instanceof DatabaseTableReference) {
                DatabaseTableReference tableRef = (DatabaseTableReference) selectedItem;
                List<ResultHeader> tableHeaders = server.getTable(tableRef).getResultHeaders();
                for (ResultHeader header : tableHeaders) {
                    newValues.add(this.evaluateHeaderValue(header));
                }
            } else {
                newValues.add(this.evaluateColumnExpr((ColumnExpression) selectedItem));
            }
        }
        return new ResultRow(newValues);
    }


    private ResultValue evaluateColumnExpr(ColumnExpression ce)
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


    private ResultValue evaluateBinaryColumnExpr(BinaryColumnExpression bce)
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


    private ResultValue evaluateColumnRef(ColumnRef cr)
            throws SqlException {

        if (!groupedByColumns.isEmpty() && !groupedByColumns.contains(new ResultHeader(cr))) {
            throw new InvalidQueryException("Column can not be used outside aggregate " +
                    "function: " + cr.getColumnName());
        }
        return rows.iterator().next().evaluateColumnExpr(cr);
    }

    private ResultValue evaluateHeaderValue(ResultHeader header)
            throws SqlException {

        if (!groupedByColumns.isEmpty() && !groupedByColumns.contains(header)) {
            throw new InvalidQueryException("Column can not be used outside aggregate " +
                    "function: " + header.getColumnName());
        }
        return rows.iterator().next().evaluateHeaderValue(header);
    }

    private ResultValue evaluateAggregateFunction(AggregateFunction af)
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

    private ResultValue getCount(ColumnRef cr)
            throws NoSuchColumnException, AmbiguousColumnNameException {
        int count = 0;
        for (ResultRow row : rows) {
            if (cr.getColumnName().isEmpty() ||
                    row.evaluateColumnRef(cr).getValue() != null) {
                count++;
            }
        }
        return new ResultValue(count);
    }

    private Collection<ResultValue> getValues(ColumnRef cr)
            throws NoSuchColumnException, AmbiguousColumnNameException,
            WrongValueTypeException {

        Collection<ResultValue> values = new ArrayList<>();
        for (ResultRow row : rows) {
            ResultValue value = row.evaluateColumnRef(cr);
            if (value.getValue() == null) {
                //"Null value can not be used in aggregate function except for COUNT(*)");
                throw new WrongValueTypeException();
            }
            values.add(value);
        }
        return values;
    }

    private ResultValue getSum(ColumnRef cr)
            throws NoSuchColumnException, AmbiguousColumnNameException,
            WrongValueTypeException {

        Collection<ResultValue> values = this.getValues(cr);
        if (values.isEmpty()) {
            return ResultValue.nullValue();
        }
        ResultValue sumValue = new ResultValue(0);
        for (ResultValue value : values) {
            if (value.isNull()) {
                continue;
            }
            if (sumValue == null) {
                sumValue = value;
                continue;
            }
            sumValue = sumValue.add(value);
        }
        return sumValue;
    }

    private ResultValue getAvg(ColumnRef cr)
            throws NoSuchColumnException, AmbiguousColumnNameException,
            WrongValueTypeException {

        Collection<ResultValue> values = this.getValues(cr);
        if (values.isEmpty()) {
            return ResultValue.nullValue();
        }
        ResultValue sumValue = null;
        for (ResultValue value : values) {
            if (value.isNull()) {
                continue;
            }
            if (sumValue == null) {
                sumValue = value;
                continue;
            }
            sumValue = sumValue.add(value);
        }
        return sumValue.divide(new ResultValue(values.size()));
    }

    private ResultValue getMax(ColumnRef cr)
            throws NoSuchColumnException, AmbiguousColumnNameException,
            WrongValueTypeException {
        Collection<ResultValue> values = this.getValues(cr);
        if (values.isEmpty()) {
            return ResultValue.nullValue();
        }
        ResultValue maxValue = null;

        for (ResultValue value : values) {
            if (value.isNull()) {
                continue;
            }
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

    private ResultValue getMin(ColumnRef cr)
            throws NoSuchColumnException, AmbiguousColumnNameException,
            WrongValueTypeException {
        Collection<ResultValue> values = this.getValues(cr);
        if (values.isEmpty()) {
            return ResultValue.nullValue();
        }
        ResultValue minValue = null;
        for (ResultValue value : values) {
            if (value.isNull()) {
                continue;
            }
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


}
