package localFileDatabase.server.intermediateResult;

import localFileDatabase.server.queryResult.QueryResultImpl;
import localFileDatabase.server.queryResult.QueryResultRowImpl;
import sqlapi.columnExpr.ColumnExpression;
import sqlapi.exceptions.SqlException;
import sqlapi.predicates.Predicate;
import sqlapi.queryResult.QueryResultRow;

import java.util.*;
import java.util.stream.Collectors;

public final class DataSet {


    private final List<DataHeader> columns;

    private final Collection<DataRow> rows;

    public DataSet(List<DataHeader> columns, Collection<DataRow> rows) {
        this.columns = Collections.unmodifiableList(columns);
        this.rows = Collections.unmodifiableCollection(rows);
    }


    public List<DataHeader> getColumns() {
        return columns;
    }

    public Collection<DataRow> getRows() {
        return rows;
    }

    public DataSet productWith(DataSet otherSet) {

        List<DataHeader> newColumns = new ArrayList<>(columns);
        newColumns.addAll(otherSet.getColumns());
        List<DataRow> newRows = new ArrayList<>();
        for (DataRow row : rows) {
            for (DataRow otherRow : otherSet.getRows()) {
                List<DataValue> newValues = new ArrayList<>(row.getValues());
                newValues.addAll(otherRow.getValues());
                newRows.add(new DataRow(newValues));
            }
        }
        return new DataSet(newColumns, newRows);
    }

    public DataSet getFilteredResult(Predicate predicate)
            throws SqlException {

        List<DataRow> newRows = new ArrayList<>();
        for (DataRow row : rows) {
            if (row.matchPredicate(predicate)) {
                newRows.add(row);
            }
        }
        return new DataSet(columns, newRows);
    }

    public DataSet innerJoin(DataSet right, Predicate sc)
            throws SqlException {

        DataSet dataSet = this.productWith(right);
        return dataSet.getFilteredResult(sc);
    }

    public DataSet leftOuterJoin(DataSet right, Predicate sc)
            throws SqlException {

        List<DataHeader> newColumns = new ArrayList<>();
        newColumns.addAll(columns);
        newColumns.addAll(right.getColumns());
        List<DataRow> rows = new ArrayList<>();
        for (DataRow leftRow : this.getRows()) {
            boolean matchFound = false;
            for (DataRow rightRow : right.getRows()) {
                List<DataValue> values = new ArrayList<>(leftRow.getValues());
                values.addAll(rightRow.getValues());
                DataRow row = new DataRow(values);
                if (row.matchPredicate(sc)) {
                    rows.add(row);
                    matchFound = true;
                }
            }
            if (!matchFound) {
                List<DataValue> values =
                        new ArrayList<>(leftRow.getValues());
                for (DataHeader header : right.getColumns()) {
                    values.add(new DataValue(header, null));
                }
                rows.add(new DataRow(values));
            }
        }
        return new DataSet(newColumns, rows);
    }

    public DataSet rightOuterJoin(DataSet right, Predicate sc)
            throws SqlException {

        List<DataHeader> newColumns = new ArrayList<>();
        newColumns.addAll(columns);
        newColumns.addAll(right.getColumns());
        List<DataRow> rows = new ArrayList<>();
        for (DataRow rightRow : right.getRows()) {
            boolean matchFound = false;
            for (DataRow leftRow : this.getRows()) {
                List<DataValue> values = new ArrayList<>(leftRow.getValues());
                values.addAll(rightRow.getValues());
                DataRow row = new DataRow(values);
                if (row.matchPredicate(sc)) {
                    rows.add(row);
                    matchFound = true;
                }
            }
            if (!matchFound) {
                List<DataValue> values = new ArrayList<>();
                for (DataHeader header : columns) {
                    values.add(new DataValue(header, null));
                }
                values.addAll(rightRow.getValues());
                rows.add(new DataRow(values));
            }
        }
        return new DataSet(newColumns, rows);
    }

    public QueryResultImpl createResultSet() {

        List<QueryResultRow> queryResultRows = new ArrayList<>();
        List<String> resultColumns = columns.stream()
                .map(DataHeader::getColumnName).collect(Collectors.toList());

        for (DataRow row : rows) {
            List<Object> values = new ArrayList<>();
            for (DataValue value : row.getValues()) {
                values.add(value.getValue());
            }
            queryResultRows.add(new QueryResultRowImpl(values));
        }
        return new QueryResultImpl(queryResultRows, resultColumns);
    }

    public List<DataRow> getSelectedValues(
            List<DataHeader> columns,
            List<ColumnExpression> columnExpressions)
            throws SqlException {

        List<DataRow> resultRows = new ArrayList<>();
        for (DataRow row : rows) {
            List<DataValue> values = new ArrayList<>();
            for (int i = 0; i < columns.size(); i++) {
                values.add(new DataValue(columns.get(i),
                        row.evaluateColumnExpr(columnExpressions.get(i)).getValue()));
            }
            resultRows.add(new DataRow(values));
        }
        return resultRows;
    }


}
