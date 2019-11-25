package serverLocalFileImpl.intermediateResult;

import serverLocalFileImpl.queryResult.ResultRowImpl;
import serverLocalFileImpl.queryResult.ResultSetImpl;
import sqlapi.columnExpr.ColumnExpression;
import sqlapi.columnExpr.ColumnRef;
import sqlapi.exceptions.AmbiguousColumnNameException;
import sqlapi.exceptions.InvalidQueryException;
import sqlapi.exceptions.NoSuchColumnException;
import sqlapi.exceptions.WrongValueTypeException;
import sqlapi.predicates.Predicate;
import sqlapi.selectResult.ResultRow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DataUtil {

    private DataUtil() {
    }

    public static DataSet getFilteredResult(DataSet resultSet,
                                            Predicate predicate)
            throws NoSuchColumnException, WrongValueTypeException,
            AmbiguousColumnNameException, InvalidQueryException {
        List<DataHeader> columns = new ArrayList<>(resultSet.getColumns());
        List<DataRow> rows = new ArrayList<>();
        for (DataRow row : resultSet.getRows()) {
            if (row.evaluatePredicate(predicate)) {
                rows.add(row);
            }
        }
        return new DataSet(columns, rows);
    }

    public static DataSet innerJoin(DataSet left,
                                    DataSet right, Predicate sc)
            throws NoSuchColumnException, WrongValueTypeException,
            AmbiguousColumnNameException, InvalidQueryException {

        DataSet dataSet = left.joinWith(right);
        return getFilteredResult(dataSet, sc);
    }

    public static DataSet leftOuterJoin(DataSet left,
                                        DataSet right, Predicate sc)
            throws NoSuchColumnException, WrongValueTypeException,
            AmbiguousColumnNameException, InvalidQueryException {

        List<DataHeader> columns = new ArrayList<>();
        columns.addAll(left.getColumns());
        columns.addAll(right.getColumns());
        List<DataRow> rows = new ArrayList<>();
        for (DataRow leftRow : left.getRows()) {
            boolean matchFound = false;
            for (DataRow rightRow : right.getRows()) {
                Map<DataHeader, Object> values =
                        new HashMap<>(leftRow.getCells());
                values.putAll(rightRow.getCells());
                DataRow row = new DataRow(values);
                if (row.evaluatePredicate(sc)) {
                    rows.add(row);
                    matchFound = true;
                }
            }
            if (!matchFound) {
                Map<DataHeader, Object> values =
                        new HashMap<>(leftRow.getCells());
                rows.add(new DataRow(values));
            }
        }
        return new DataSet(columns, rows);
    }

    public static DataSet rightOuterJoin(DataSet left,
                                         DataSet right, Predicate sc)
            throws NoSuchColumnException, WrongValueTypeException,
            AmbiguousColumnNameException, InvalidQueryException {

        List<DataHeader> columns = new ArrayList<>();
        columns.addAll(left.getColumns());
        columns.addAll(right.getColumns());
        List<DataRow> rows = new ArrayList<>();
        for (DataRow rightRow : right.getRows()) {
            boolean matchFound = false;
            for (DataRow leftRow : left.getRows()) {
                Map<DataHeader, Object> values =
                        new HashMap<>(leftRow.getCells());
                values.putAll(rightRow.getCells());
                DataRow row = new DataRow(values);
                if (row.evaluatePredicate(sc)) {
                    rows.add(row);
                    matchFound = true;
                }
            }
            if (!matchFound) {
                Map<DataHeader, Object> values =
                        new HashMap<>(rightRow.getCells());
                rows.add(new DataRow(values));
            }
        }
        return new DataSet(columns, rows);
    }

    public static ResultSetImpl createResultSet(DataSet dataSet) {

        List<ResultRow> resultRows = new ArrayList<>();
        List<String> resultColumns = dataSet.getColumns().stream()
                .map(DataHeader::getColumnName).collect(Collectors.toList());

        for (DataRow row : dataSet.getRows()) {
            List<Object> values = new ArrayList<>();
            for (DataHeader cr : dataSet.getColumns()) {
                values.add(row.getCells().get(cr));
            }
            resultRows.add(new ResultRowImpl(values));
        }
        return new ResultSetImpl(resultRows, resultColumns);
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
            throws NoSuchColumnException, AmbiguousColumnNameException,
            InvalidQueryException {

        List<DataRow> resultRows = new ArrayList<>();
        for (DataRow row : dataSet.getRows()) {
            Map<DataHeader, Object> values = new HashMap<>();
            for (int i = 0; i < columns.size(); i++) {
                values.put(columns.get(i),
                        row.evaluateColumnExpr(columnExpressions.get(i)));
            }
            resultRows.add(new DataRow(values));
        }
        return resultRows;
    }
}
