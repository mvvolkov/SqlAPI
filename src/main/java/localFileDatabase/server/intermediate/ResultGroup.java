package localFileDatabase.server.intermediate;

import localFileDatabase.server.LocalFileDbServer;
import org.jetbrains.annotations.NotNull;
import sqlapi.columnExpr.*;
import sqlapi.exceptions.*;
import sqlapi.misc.SelectedItem;
import sqlapi.tables.DatabaseTableReference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Class for group of rows.
 */
final class ResultGroup extends AbstractResultRow {

    @NotNull
    private final LocalFileDbServer server;

    @NotNull
    private final Collection<ResultHeader> groupedByColumns;

    @NotNull
    private final Collection<ResultRow> rows;


    ResultGroup(@NotNull LocalFileDbServer server,
                @NotNull Collection<ResultHeader> groupedByColumns,
                @NotNull Collection<ResultRow> rows) {
        this.server = server;
        this.groupedByColumns = groupedByColumns;
        this.rows = rows;
    }


    @NotNull
    ResultRow getSelectedValues(@NotNull List<SelectedItem> selectedItems,
                                @NotNull List<ResultHeader> headers) throws SqlException {

        List<ResultValue> newValues = new ArrayList<>();
        if (selectedItems.isEmpty()) {
            for (ResultHeader header : headers) {
                newValues.add(this.evaluateHeaderValue(header));
            }
        }
        for (SelectedItem selectedItem : selectedItems) {
            if (selectedItem instanceof DatabaseTableReference) {
                DatabaseTableReference tableRef = (DatabaseTableReference) selectedItem;
                List<ResultHeader> tableHeaders =
                        server.getTable(tableRef).getResultHeaders();
                for (ResultHeader header : tableHeaders) {
                    newValues.add(this.evaluateHeaderValue(header));
                }
            } else {
                newValues.add(this.evaluateColumnExpr((ColumnExpression) selectedItem));
            }
        }
        return new ResultRow(newValues);
    }


    @NotNull
    protected ResultValue evaluateColumnRef(@NotNull ColumnRef cr)
            throws SqlException {

        if (!groupedByColumns.isEmpty() &&
                !groupedByColumns.contains(new ResultHeader(cr.getColumnName()))) {
            throw new InvalidQueryException("Column can not be used outside aggregate " +
                    "function: " + cr.getColumnName());
        }
        return rows.iterator().next().evaluateColumnExpr(cr);
    }

    @NotNull
    private ResultValue evaluateHeaderValue(@NotNull ResultHeader header)
            throws SqlException {

        if (!groupedByColumns.isEmpty() &&
                !groupedByColumns.contains(new ResultHeader(header.getColumnName()))) {
            throw new InvalidQueryException("Column can not be used outside aggregate " +
                    "function: " + header.getColumnName());
        }
        return rows.iterator().next().evaluateHeaderValue(header);
    }

    @NotNull
    protected ResultValue evaluateAggregateFunction(@NotNull AggregateFunction af)
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

    @NotNull
    private ResultValue getCount(@NotNull ColumnRef cr)
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

    @NotNull
    private Collection<ResultValue> getColumnValues(@NotNull ColumnRef cr)
            throws NoSuchColumnException, AmbiguousColumnNameException {

        Collection<ResultValue> values = new ArrayList<>();
        for (ResultRow row : rows) {
            ResultValue value = row.evaluateColumnRef(cr);
            if (value.getValue() != null) {
                values.add(value);
            }
        }
        return values;
    }

    @NotNull
    private ResultValue getSum(@NotNull ColumnRef cr)
            throws NoSuchColumnException, AmbiguousColumnNameException,
            WrongValueTypeException {

        ResultValue sumValue = ResultValue.nullValue();
        for (ResultValue value : this.getColumnValues(cr)) {
            if (sumValue.isNull()) {
                sumValue = value;
                continue;
            }
            sumValue = sumValue.add(value);
        }
        return sumValue;
    }

    @NotNull
    private ResultValue getAvg(@NotNull ColumnRef cr)
            throws NoSuchColumnException, AmbiguousColumnNameException,
            WrongValueTypeException {

        Collection<ResultValue> values = this.getColumnValues(cr);
        return this.getSum(cr).divide(new ResultValue(values.size()));
    }

    @NotNull
    private ResultValue getMax(@NotNull ColumnRef cr)
            throws SqlException {

        ResultValue maxValue = ResultValue.nullValue();
        for (ResultValue resultValue : this.getColumnValues(cr)) {
            if (maxValue.isNull() && !resultValue.isNull()) {
                maxValue = resultValue;
                continue;
            }
            if (maxValue.getValue() == null) {
                continue;
            }
            if (resultValue.getComparisonResult(maxValue.getValue()) > 0) {
                maxValue = resultValue;
            }
        }
        return maxValue;
    }

    @NotNull
    private ResultValue getMin(@NotNull ColumnRef cr)
            throws SqlException {

        ResultValue minValue = ResultValue.nullValue();
        for (ResultValue value : this.getColumnValues(cr)) {
            if (minValue.isNull()) {
                minValue = value;
                continue;
            }
            if (minValue.getValue() == null) {
                continue;
            }
            if (value.getComparisonResult(minValue.getValue()) < 0) {
                minValue = value;
            }
        }
        return minValue;
    }
}
