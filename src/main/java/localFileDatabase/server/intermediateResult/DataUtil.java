package localFileDatabase.server.intermediateResult;

import localFileDatabase.server.queryResult.QueryResultImpl;
import localFileDatabase.server.queryResult.QueryResultRowImpl;
import sqlapi.columnExpr.ColumnExpression;
import sqlapi.columnExpr.ColumnRef;
import sqlapi.exceptions.SqlException;
import sqlapi.predicates.Predicate;
import sqlapi.queryResult.QueryResultRow;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DataUtil {

    private DataUtil() {
    }

    public static DataSet getFilteredResult(DataSet resultSet,
                                            Predicate predicate)
            throws SqlException {
        List<DataHeader> columns = new ArrayList<>(resultSet.getColumns());
        List<DataRow> rows = new ArrayList<>();
        for (DataRow row : resultSet.getRows()) {
            if (row.matchPredicate(predicate)) {
                rows.add(row);
            }
        }
        return new DataSet(columns, rows);
    }

    public static DataSet innerJoin(DataSet left,
                                    DataSet right, Predicate sc)
            throws SqlException {

        DataSet dataSet = left.productWith(right);
        return getFilteredResult(dataSet, sc);
    }

    public static DataSet leftOuterJoin(DataSet left,
                                        DataSet right, Predicate sc)
            throws SqlException {

        List<DataHeader> columns = new ArrayList<>();
        columns.addAll(left.getColumns());
        columns.addAll(right.getColumns());
        List<DataRow> rows = new ArrayList<>();
        for (DataRow leftRow : left.getRows()) {
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
                rows.add(new DataRow(values));
            }
        }
        return new DataSet(columns, rows);
    }

    public static DataSet rightOuterJoin(DataSet left,
                                         DataSet right, Predicate sc)
            throws SqlException {

        List<DataHeader> columns = new ArrayList<>();
        columns.addAll(left.getColumns());
        columns.addAll(right.getColumns());
        List<DataRow> rows = new ArrayList<>();
        for (DataRow rightRow : right.getRows()) {
            boolean matchFound = false;
            for (DataRow leftRow : left.getRows()) {
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
                        new ArrayList<>(rightRow.getValues());
                rows.add(new DataRow(values));
            }
        }
        return new DataSet(columns, rows);
    }

    public static QueryResultImpl createResultSet(DataSet dataSet) {

        List<QueryResultRow> queryResultRows = new ArrayList<>();
        List<String> resultColumns = dataSet.getColumns().stream()
                .map(DataHeader::getColumnName).collect(Collectors.toList());

        for (DataRow row : dataSet.getRows()) {
            List<Object> values = new ArrayList<>();
            for (DataValue value : row.getValues()) {
                values.add(value.getValue());
            }
            queryResultRows.add(new QueryResultRowImpl(values));
        }
        return new QueryResultImpl(queryResultRows, resultColumns);
    }

    public static List<DataHeader> getSelectedColumns(
            List<ColumnExpression> columnExpressions) {

        List<DataHeader> resultColumns = new ArrayList<>();
        for (ColumnExpression columnExpression : columnExpressions) {
            if (columnExpression instanceof ColumnRef) {
                resultColumns.add(new DataHeader((ColumnRef) columnExpression));
            } else if (!columnExpression.getAlias().isEmpty()) {
                resultColumns
                        .add(new DataHeader((columnExpression).getAlias()));
            } else {
                resultColumns.add(new DataHeader(columnExpression.toString()));
            }
        }
        return resultColumns;
    }

    public static List<DataRow> getSelectedValues(
            DataSet dataSet, List<DataHeader> columns,
            List<ColumnExpression> columnExpressions)
            throws SqlException {

        List<DataRow> resultRows = new ArrayList<>();
        for (DataRow row : dataSet.getRows()) {
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
