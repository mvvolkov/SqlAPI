package SimpleFileImpl;

import SimplePrintOutImpl.SqlManagerPrintOutImpl;
import org.jetbrains.annotations.NotNull;
import sqlapi.*;
import sqlapi.ColumnMetadata;
import sqlapi.exceptions.*;
import sqlapi.selectionPredicate.*;
import sqlapi.selectionResult.ResultRow;
import sqlapi.selectionResult.ResultSet;
import sqlapi.selectionResult.ResultValue;
import sqlapi.tableReference.BaseTableReference;
import sqlapi.tableReference.JoinTableReference;
import sqlapi.tableReference.SelectExpression;
import sqlapi.tableReference.TableReference;

import java.util.*;

public class SqlManagerImpl implements SqlManager {


    SqlManagerPrintOutImpl logger = new SqlManagerPrintOutImpl();

    private final Collection<Database> databases = new ArrayList<>();

    public SqlManagerPrintOutImpl getLogger() {
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

    private static ResultSet joinResultSets(List<ResultSet> resultSets, SelectionPredicate selectionPredicate) throws NoSuchColumnException, WrongValueTypeException {

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

    private static ResultSet innerJoin(ResultSet left, ResultSet right, SelectionPredicate sc) throws NoSuchColumnException, WrongValueTypeException {

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

    private static ResultSet leftOutJoin(ResultSet left, ResultSet right, SelectionPredicate sc) throws NoSuchColumnException, WrongValueTypeException {

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

    private static ResultSet rightOutJoin(ResultSet left, ResultSet right, SelectionPredicate sc) throws NoSuchColumnException, WrongValueTypeException {

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

    static boolean evaluate(ResultRow resultRow, SelectionPredicate sc) throws NoSuchColumnException, WrongValueTypeException {

        if (sc.isEmpty()) {
            return true;
        }
        switch (sc.getType()) {
            case AND:
                CombinedPredicate cp1 = (CombinedPredicate) sc;
                return evaluate(resultRow, cp1.getLeftPredicate()) &&
                        evaluate(resultRow, cp1.getRightPredicate());
            case OR:
                CombinedPredicate cp2 = (CombinedPredicate) sc;
                return evaluate(resultRow, cp2.getLeftPredicate()) ||
                        evaluate(resultRow, cp2.getRightPredicate());
            case EQUALS:
            case NOT_EQUALS:
            case GREATER_THAN:
            case GREATER_THAN_OR_EQUALS:
            case LESS_THAN:
            case LESS_THAN_OR_EQUALS:
                return compareValues(resultRow, sc);
            case IS_NULL:
                ColumnIsNullPredicate cn1 = (ColumnIsNullPredicate) sc;
                return resultRow.getValue(cn1.getColumnReference().getColumnName()).isNull();
            case IS_NOT_NULL:
                ColumnIsNullPredicate cn2 = (ColumnIsNullPredicate) sc;
                return resultRow.getValue(cn2.getColumnReference().getColumnName()).isNotNull();
            default:
                return false;
        }
    }

    static boolean compareValues(ResultRow resultRow, SelectionPredicate sc) throws WrongValueTypeException, NoSuchColumnException {

        Comparable leftValue = null;
        Comparable rightValue = null;
        ColumnReference cr = null;

        if (sc instanceof OneColumnPredicate) {
            cr = ((OneColumnPredicate) sc).getColumnReference();
            leftValue = resultRow.getValue(cr.getColumnName()).getValue();
            rightValue = ((OneColumnPredicate) sc).getValue();
        }

        if (sc instanceof TwoColumnsPredicate) {
            cr = ((TwoColumnsPredicate) sc).getLeftColumn();
            leftValue = resultRow.getValue(cr.getColumnName()).getValue();
            ColumnReference cr2 = ((TwoColumnsPredicate) sc).getRightColumn();
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
        return table.select(Arrays.asList(SelectedColumn.all()), SelectionPredicate.empty());
    }

    protected ResultSet selectFromJoinedTable(TableReference tableReference)
            throws NoSuchTableException, NoSuchDatabaseException, NoSuchColumnException, WrongValueTypeException {
        if (!(tableReference instanceof JoinTableReference)) {
            throw new IllegalArgumentException();
        }
        JoinTableReference jto = (JoinTableReference) tableReference;
        ResultSet left = this.select(SelectExpression.builder(jto.getLeft()).build());
        ResultSet right = this.select(SelectExpression.builder(jto.getRight()).build());
        switch (jto.getType()) {
            case INNER_JOIN:
                return innerJoin(left, right, jto.getSelectionPredicate());
            case LEFT_OUTER_JOIN:
                return leftOutJoin(left, right, jto.getSelectionPredicate());
            case RIGHT_OUTER_JOIN:
                return rightOutJoin(left, right, jto.getSelectionPredicate());
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
                resultSets.add(this.selectFromJoinedTable(tableReference));
                continue;
            } else if (tableReference instanceof SelectExpression) {
                resultSets.add(this.select((SelectExpression) tableReference));
            }
        }
        ResultSet resultSet = this.joinResultSets(resultSets, selectExpression.getSelectionPredicate());
        return resultSet;
    }

    protected Table getTable(BaseTableReference tableReference) throws
            NoSuchTableException, NoSuchDatabaseException {
        for (Database database : databases) {
            if (database.getName().equals(tableReference.getDbName())) {
                return (TableImpl) database.getTable(tableReference.getTableName());
            }
        }
        throw new NoSuchDatabaseException(tableReference.getDbName());
    }

}
