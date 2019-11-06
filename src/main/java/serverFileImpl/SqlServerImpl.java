package serverFileImpl;

import api.selectionPredicate.*;
import clientDefaultImpl.*;
import serverPrintOutImpl.SqlServerPrintOutImpl;
import org.jetbrains.annotations.NotNull;
import api.*;
import api.ColumnMetadata;
import api.exceptions.*;
import api.selectionResult.ResultRow;
import api.selectionResult.ResultSet;
import api.selectionResult.ResultValue;


import java.util.*;

public class SqlServerImpl implements SqlServer {


    SqlServerPrintOutImpl logger = new SqlServerPrintOutImpl();

    private final Collection<Database> databases = new ArrayList<>();

    public SqlServerPrintOutImpl getLogger() {
        return logger;
    }

    @Override
    public void createDatabase(String dbName) throws DatabaseAlreadyExistsException {
        for (Database database : databases) {
            if (database.getName().equals(dbName)) {
                throw new DatabaseAlreadyExistsException(dbName);
            }
        }
        databases.add(new DatabaseImpl(this, dbName));
        try {
            this.getLogger().createDatabase(dbName);
        } catch (DatabaseAlreadyExistsException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void openDatabaseWithTables(String dbName, List<TableMetadata> tables) {
    }


    @Override
    public void persistDatabase(String dbName) {
    }

    @Override
    public Database getDatabaseOrNull(String dbName) {
        for (Database database : databases) {
            if (database.getName().equals(dbName)) {
                return database;
            }
        }
        return null;
    }

    @Override
    public @NotNull Database getDatabase(String dbName) throws NoSuchDatabaseException {
        Database database = this.getDatabaseOrNull(dbName);
        if (database == null) {
            throw new NoSuchDatabaseException(dbName);
        }
        return database;
    }

    private static List<ResultValue> getValuesFromResultSets(List<ResultSet> resultSets, Deque<Integer> stack) {
        Deque<Integer> rowNumbers = new ArrayDeque<>(stack);
        List<ResultValue> values = new ArrayList<>();
        while (!rowNumbers.isEmpty()) {
            int rowNumber = rowNumbers.pop();
            ResultSet resultSet = resultSets.get(rowNumbers.size());
            ResultRow row = resultSet.getRow(rowNumber);
            values.addAll(row.getValues());
        }
        return values;
    }

    private static ResultSet joinResultSets(List<ResultSet> resultSets, Predicate selectionPredicate) throws NoSuchColumnException, WrongValueTypeException {

        // Create full list of columns.
        List<ColumnMetadata> columns = new ArrayList<>();
        for (ResultSet resultSet : resultSets) {
            columns.addAll(resultSet.getColumns());
        }

        if (resultSets.size() == 1) {
            ResultSet resultSet = resultSets.get(0);
            List<ResultRow> rows = new ArrayList<>();
            for (ResultRow row : resultSet.getAllRows()) {
                if (evaluate(row, selectionPredicate)) {
                    rows.add(row);
                }
            }
            return new ResultSetImpl(rows, columns);
        }

        // Create all possible combinations of rows from different result sets.
        List<ResultRow> rows = new ArrayList<>();
        // Use stack here to get all combinations.
        Deque<Integer> rowNumbers = new ArrayDeque<>(resultSets.size());
        rowNumbers.push(0);
        boolean nextResultSetHasNoRows = false;
        while (!rowNumbers.isEmpty()) {
            if (rowNumbers.size() == resultSets.size() || nextResultSetHasNoRows) {
                int rowNumber = rowNumbers.pop();
                nextResultSetHasNoRows = (rowNumber == resultSets.get(rowNumbers.size()).getSize() - 1);
                if (nextResultSetHasNoRows) {
                    continue;
                }
                rowNumbers.push(++rowNumber);
            }
            while (rowNumbers.size() < resultSets.size()) {
                rowNumbers.push(0);
            }
            ResultRow row = new ResultRowImpl(getValuesFromResultSets(resultSets, rowNumbers));
            if (evaluate(row, selectionPredicate)) {
                rows.add(row);
            }
        }
        return new ResultSetImpl(rows, columns);
    }

    private static ResultSet innerJoin(ResultSet left, ResultSet right, Predicate sc) throws NoSuchColumnException, WrongValueTypeException {

        List<ColumnMetadata> columns = new ArrayList<>();
        columns.addAll(left.getColumns());
        columns.addAll(right.getColumns());
        List<ResultRow> rows = new ArrayList<>();
        for (ResultRow leftRow : left.getAllRows()) {
            for (ResultRow rightRow : right.getAllRows()) {
                List<ResultValue> values = new ArrayList<>();
                values.addAll(leftRow.getValues());
                values.addAll(rightRow.getValues());
                ResultRow row = new ResultRowImpl(values);
                if (evaluate(row, sc)) {
                    rows.add(row);
                }
            }
        }
        return new ResultSetImpl(rows, columns);
    }

    private static ResultSet leftOutJoin(ResultSet left, ResultSet right, Predicate sc) throws NoSuchColumnException, WrongValueTypeException {

        List<ColumnMetadata> columns = new ArrayList<>();
        columns.addAll(left.getColumns());
        columns.addAll(right.getColumns());
        List<ResultRow> rows = new ArrayList<>();
        for (ResultRow leftRow : left.getAllRows()) {
            boolean matchFound = false;
            for (ResultRow rightRow : right.getAllRows()) {
                List<ResultValue> values = new ArrayList<>();
                values.addAll(leftRow.getValues());
                values.addAll(rightRow.getValues());
                ResultRow row = new ResultRowImpl(values);
                if (evaluate(row, sc)) {
                    rows.add(row);
                    matchFound = true;
                }
            }
            if (!matchFound) {
                List<ResultValue> values = new ArrayList<>();
                values.addAll(leftRow.getValues());
                for (ColumnMetadata columnMetadata : right.getColumns()) {
                    values.add(new ResultValueImpl(null, columnMetadata.getName()));
                }
            }
        }
        return new ResultSetImpl(rows, columns);
    }

    private static ResultSet rightOutJoin(ResultSet left, ResultSet right, Predicate sc) throws NoSuchColumnException, WrongValueTypeException {

        List<ColumnMetadata> columns = new ArrayList<>();
        columns.addAll(left.getColumns());
        columns.addAll(right.getColumns());
        List<ResultRow> rows = new ArrayList<>();
        for (ResultRow rightRow : right.getAllRows()) {
            boolean matchFound = false;
            for (ResultRow leftRow : left.getAllRows()) {
                List<ResultValue> values = new ArrayList<>();
                values.addAll(leftRow.getValues());
                values.addAll(rightRow.getValues());
                ResultRow row = new ResultRowImpl(values);
                if (evaluate(row, sc)) {
                    rows.add(row);
                    matchFound = true;
                }
            }
            if (!matchFound) {
                List<ResultValue> values = new ArrayList<>();
                for (ColumnMetadata columnMetadata : left.getColumns()) {
                    values.add(new ResultValueImpl(null, columnMetadata.getName()));
                }
                values.addAll(rightRow.getValues());
            }
        }
        return new ResultSetImpl(rows, columns);
    }

    static boolean evaluate(ResultRow resultRow, Predicate predicate) throws NoSuchColumnException, WrongValueTypeException {

        if (predicate.getType() == Predicate.Type.TRUE) {
            return true;
        } else if (predicate.getType() == Predicate.Type.FALSE) {
            return false;
        }
        switch (predicate.getType()) {
            case AND:
                CombinedPredicate cp1 = (CombinedPredicate) predicate;
                return evaluate(resultRow, cp1.getLeftPredicate()) &&
                        evaluate(resultRow, cp1.getRightPredicate());
            case OR:
                CombinedPredicate cp2 = (CombinedPredicate) predicate;
                return evaluate(resultRow, cp2.getLeftPredicate()) ||
                        evaluate(resultRow, cp2.getRightPredicate());
            case EQUALS:
            case NOT_EQUALS:
            case GREATER_THAN:
            case GREATER_THAN_OR_EQUALS:
            case LESS_THAN:
            case LESS_THAN_OR_EQUALS:
                return compareValues(resultRow, predicate);
            case IS_NULL:
                ColumnNullPredicate cn1 = (ColumnNullPredicate) predicate;
                return resultRow.getValue(cn1.getColumnReference().getColumnName()).isNull();
            case IS_NOT_NULL:
                ColumnNullPredicate cn2 = (ColumnNullPredicate) predicate;
                return resultRow.getValue(cn2.getColumnReference().getColumnName()).isNotNull();
            default:
                return false;
        }
    }

    static boolean compareValues(ResultRow resultRow, Predicate sc) throws WrongValueTypeException, NoSuchColumnException {

        Comparable leftValue = null;
        Comparable rightValue = null;
        ColumnReference cr = null;

        if (sc instanceof ColumnValuePredicate) {
            cr = ((ColumnValuePredicate) sc).getColumnReference();
            leftValue = resultRow.getValue(cr.getColumnName()).getValue();
            rightValue = ((ColumnValuePredicate) sc).getValue();
        }

        if (sc instanceof ColumnColumnPredicate) {
            cr = ((ColumnColumnPredicate) sc).getLeftColumn();
            leftValue = resultRow.getValue(cr.getColumnName()).getValue();
            ColumnReference cr2 = ((ColumnColumnPredicate) sc).getRightColumn();
            rightValue = resultRow.getValue(cr2.getColumnName()).getValue();
        }

        if (leftValue == null || rightValue == null) {
            return false;
        }

        if (!leftValue.getClass().equals(rightValue.getClass())) {
            throw new WrongValueTypeException(cr, leftValue.getClass(), rightValue.getClass());
        }


        int compResult = leftValue.compareTo(rightValue);
        switch (sc.getType()) {
            case EQUALS:
                return compResult == 0;
            case NOT_EQUALS:
                return compResult != 0;
            case GREATER_THAN:
                return compResult > 0;
            case GREATER_THAN_OR_EQUALS:
                return compResult >= 0;
            case LESS_THAN:
                return compResult < 0;
            case LESS_THAN_OR_EQUALS:
                return compResult <= 0;
            default:
                return false;
        }
    }

    protected ResultSet selectFromBaseTable(TableReference tableReference)
            throws NoSuchTableException, NoSuchDatabaseException, WrongValueTypeException {
        if (!(tableReference instanceof BaseTableReference)) {
            throw new IllegalArgumentException();
        }
        TableImpl table = (TableImpl) this.getTable((BaseTableReference) tableReference);
        return table.select(Arrays.asList(SelectedColumn.all()));
    }

    protected ResultSet selectFromJoinedTable(JoinTableReference tableReference)
            throws NoSuchTableException, NoSuchDatabaseException, NoSuchColumnException, WrongValueTypeException {


        ResultSet left = this.selectAll(tableReference.getLeftTableReference());
        ResultSet right = this.selectAll(tableReference.getRightTableReference());
        switch (tableReference.getJoinType()) {
            case INNER_JOIN:
                return innerJoin(left, right, tableReference.getSelectionPredicate());
            case LEFT_OUTER_JOIN:
                return leftOutJoin(left, right, tableReference.getSelectionPredicate());
            case RIGHT_OUTER_JOIN:
                return rightOutJoin(left, right, tableReference.getSelectionPredicate());
        }
        return new ResultSetImpl(Collections.EMPTY_LIST, Collections.EMPTY_LIST);
    }

    @Override
    public @NotNull ResultSet select(SelectExpression selectExpression) throws
            WrongValueTypeException, NoSuchTableException, NoSuchDatabaseException, NoSuchColumnException {

        List<ResultSet> resultSets = new ArrayList<>();
        for (TableReference tableReference : selectExpression.getTableReferences()) {
            if (tableReference instanceof BaseTableReference) {
                resultSets.add(this.selectFromBaseTable(tableReference));
                continue;
            } else if (tableReference instanceof JoinTableReference) {
                resultSets.add(this.selectFromJoinedTable((JoinTableReference) tableReference));
                continue;
            } else if (tableReference instanceof SelectExpression) {
                resultSets.add(this.select((SelectExpression) tableReference));
            }
        }
        ResultSet resultSet = this.joinResultSets(resultSets, selectExpression.getSelectionPredicate());
        return resultSet;
    }

    protected @NotNull ResultSet selectAll(TableReference tableReference) throws
            WrongValueTypeException, NoSuchTableException, NoSuchDatabaseException, NoSuchColumnException {

        if (tableReference instanceof BaseTableReference) {
            return this.selectFromBaseTable(tableReference);
        } else if (tableReference instanceof JoinTableReference) {
            return this.selectFromJoinedTable((JoinTableReference) tableReference);
        } else if (tableReference instanceof SelectExpression) {
            return this.select((SelectExpression) tableReference);
        }
        throw new IllegalArgumentException("");
    }

    protected Table getTable(BaseTableReference tableReference) throws
            NoSuchTableException, NoSuchDatabaseException {
        for (Database database : databases) {
            if (database.getName().equals(tableReference.getDatabaseName())) {
                return (TableImpl) database.getTable(tableReference.getTableName());
            }
        }
        throw new NoSuchDatabaseException(tableReference.getDatabaseName());
    }

}
