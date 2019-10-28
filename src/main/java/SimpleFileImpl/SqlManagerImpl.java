package SimpleFileImpl;

import SimplePrintOutImpl.SqlManagerPrintOutImpl;
import org.jetbrains.annotations.NotNull;
import sqlapi.*;
import sqlapi.dbMetadata.ColumnMetadata;
import sqlapi.dbMetadata.TableMetadata;
import sqlapi.exceptions.*;
import sqlapi.selectionPredicate.*;
import sqlapi.selectionResult.ResultRow;
import sqlapi.selectionResult.ResultSet;
import sqlapi.selectionResult.ResultValue;

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
                return compResult > -0;
            case LESS_THAN:
                return compResult < 0;
            case LESS_THAN_OR_EQUALS:
                return compResult <= 0;
            default:
                return false;
        }
    }

    @Override
    public @NotNull ResultSet select(SelectExpression selectExpression) throws WrongValueTypeException, NoSuchTableException, NoSuchDatabaseException, NoSuchColumnException {

        List<ResultSet> resultSets = new ArrayList<>();
        for (TableReference tableReference : selectExpression.getTableReferences()) {
            if (tableReference instanceof BaseTableReference) {
                BaseTableReference btr = (BaseTableReference) tableReference;
                TableImpl table = (TableImpl) this.getTable((BaseTableReference) tableReference);
                resultSets.add(table.select(Arrays.asList(SelectedColumn.all()), SelectionPredicate.empty()));
                continue;
            }
        }
        ResultSet resultSet = this.joinResultSets(resultSets, selectExpression.getSelectionPredicate());
        return resultSet;
    }

    protected Table getTable(BaseTableReference tableReference) throws NoSuchTableException, NoSuchDatabaseException {
        for (Database database : databases) {
            if (database.getName().equals(tableReference.getDbName())) {
                return (TableImpl) database.getTable(tableReference.getTableName());
            }
        }
        throw new NoSuchDatabaseException(tableReference.getDbName());
    }

}
