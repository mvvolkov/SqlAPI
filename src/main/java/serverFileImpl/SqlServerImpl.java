package serverFileImpl;

import api.*;
import api.exceptions.*;
import api.selectionPredicate.*;
import org.jetbrains.annotations.NotNull;
import serverLoggerImpl.SqlServerLoggerImpl;

import java.util.*;

public class SqlServerImpl implements SqlServer {


    private final transient SqlServerLoggerImpl logger = new SqlServerLoggerImpl();

    private final Collection<Database> databases = new ArrayList<>();


    @Override
    public void executeStatement(SqlStatement stmt) throws SqlException {
        logger.executeStatement(stmt);
        switch (stmt.getType()) {
            case CREATE_TABLE:
                this.createTable((CreateTableStatement) stmt);
                return;
            case INSERT:
                this.insert((InsertStatement) stmt);
                return;
            case DELETE:
//                delete((DeleteStatement) stmt);
                return;
            case UPDATE:
//                update((UpdateStatement) stmt);
                return;
        }
    }


    private void createTable(CreateTableStatement stmt) throws TableAlreadyExistsException, NoSuchDatabaseException {
        Database database = this.getDatabase(stmt.getDatabaseName());
        Table table = database.getTableOrNull(stmt.getTableName());
        if (table != null) {
            throw new TableAlreadyExistsException(database.getName(), stmt.getTableName());
        }
        database.addTable(new Table(database, stmt.getTableName(), stmt.getColumns()));
    }

    private void insert(InsertStatement stmt) throws NoSuchDatabaseException, NoSuchTableException, ConstraintException, WrongValueTypeException {
        Database database = this.getDatabase(stmt.getDatabaseName());
        Table table = database.getTable(stmt.getTableName());
        if (stmt.getColumns() == null) {
            table.insert(stmt.getValues());
        } else {
            table.insert(stmt.getColumns(), stmt.getValues());
        }
    }

    @Override
    public void createDatabase(String dbName) throws DatabaseAlreadyExistsException {
        logger.createDatabase(dbName);
        for (Database database : databases) {
            if (database.getName().equals(dbName)) {
                throw new DatabaseAlreadyExistsException(dbName);
            }
        }
        databases.add(new Database(dbName));
    }

    @Override
    public void openDatabaseWithTables(String dbName, List<TableMetadata> tables) {
    }


    @Override
    public void persistDatabase(String dbName) {
    }


    public Database getDatabaseOrNull(String dbName) {
        for (Database database : databases) {
            if (database.getName().equals(dbName)) {
                return database;
            }
        }
        return null;
    }


    public @NotNull Database getDatabase(String dbName) throws NoSuchDatabaseException {
        Database database = this.getDatabaseOrNull(dbName);
        if (database == null) {
            throw new NoSuchDatabaseException(dbName);
        }
        return database;
    }

    private static List<Object> getValuesFromResultSets(List<ResultSet> resultSets, Deque<Integer> stack) {
        Deque<Integer> rowNumbers = new ArrayDeque<>(stack);
        List<Object> values = new ArrayList<>();
        while (!rowNumbers.isEmpty()) {
            int rowNumber = rowNumbers.pop();
            ResultSet resultSet = resultSets.get(rowNumbers.size());
            ResultRow row = resultSet.getRows().get(rowNumber);
            values.addAll(0, row.getValues());
        }
        return values;
    }

    private static ResultSet joinResultSets(List<ResultSet> resultSets, Predicate selectionPredicate) throws NoSuchColumnException, WrongValueTypeException {

        // Create full list of columns.
        List<String> columns = new ArrayList<>();
        boolean hasEmptyResultSet = false;
        for (ResultSet resultSet : resultSets) {
            columns.addAll(resultSet.getColumns());
            if (resultSet.getRows().isEmpty()) {
                hasEmptyResultSet = true;
            }
        }
        if (hasEmptyResultSet) {
            return new ResultSetImpl(Collections.EMPTY_LIST, columns);
        }

        if (resultSets.size() == 1) {
            ResultSet resultSet = resultSets.get(0);
            List<ResultRow> rows = new ArrayList<>();
            for (ResultRow row : resultSet.getRows()) {
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
                nextResultSetHasNoRows = (rowNumber == resultSets.get(rowNumbers.size()).getRows().size() - 1);
                if (nextResultSetHasNoRows) {
                    continue;
                }
                rowNumbers.push(++rowNumber);
            }
            while (rowNumbers.size() < resultSets.size()) {
                rowNumbers.push(0);
            }
            ResultRow row = new ResultRowImpl(columns, getValuesFromResultSets(resultSets, rowNumbers));
            if (evaluate(row, selectionPredicate)) {
                rows.add(row);
            }
        }
        return new ResultSetImpl(rows, columns);
    }

    private static ResultSet innerJoin(ResultSet left, ResultSet right, Predicate sc) throws NoSuchColumnException, WrongValueTypeException {

        List<String> columns = new ArrayList<>();
        columns.addAll(left.getColumns());
        columns.addAll(right.getColumns());
        List<ResultRow> rows = new ArrayList<>();
        for (ResultRow leftRow : left.getRows()) {
            for (ResultRow rightRow : right.getRows()) {
                List<Object> values = new ArrayList<>();
                values.addAll(leftRow.getValues());
                values.addAll(rightRow.getValues());
                ResultRow row = new ResultRowImpl(columns, values);
                if (evaluate(row, sc)) {
                    rows.add(row);
                }
            }
        }
        return new ResultSetImpl(rows, columns);
    }

    private static ResultSet leftOutJoin(ResultSet left, ResultSet right, Predicate sc) throws NoSuchColumnException, WrongValueTypeException {

        List<String> columns = new ArrayList<>();
        columns.addAll(left.getColumns());
        columns.addAll(right.getColumns());
        List<ResultRow> rows = new ArrayList<>();
        for (ResultRow leftRow : left.getRows()) {
            boolean matchFound = false;
            for (ResultRow rightRow : right.getRows()) {
                List<Object> values = new ArrayList<>();
                values.addAll(leftRow.getValues());
                values.addAll(rightRow.getValues());
                ResultRow row = new ResultRowImpl(columns, values);
                if (evaluate(row, sc)) {
                    rows.add(row);
                    matchFound = true;
                }
            }
            if (!matchFound) {
                List<Object> values = new ArrayList<>();
                values.addAll(leftRow.getValues());
                for (String columnName : right.getColumns()) {
                    values.add(null);
                }
            }
        }
        return new ResultSetImpl(rows, columns);
    }

    private static ResultSet rightOutJoin(ResultSet left, ResultSet right, Predicate sc) throws NoSuchColumnException, WrongValueTypeException {

        List<String> columns = new ArrayList<>();
        columns.addAll(left.getColumns());
        columns.addAll(right.getColumns());
        List<ResultRow> rows = new ArrayList<>();
        for (ResultRow rightRow : right.getRows()) {
            boolean matchFound = false;
            for (ResultRow leftRow : left.getRows()) {
                List<Object> values = new ArrayList<>();
                values.addAll(leftRow.getValues());
                values.addAll(rightRow.getValues());
                ResultRow row = new ResultRowImpl(columns, values);
                if (evaluate(row, sc)) {
                    rows.add(row);
                    matchFound = true;
                }
            }
            if (!matchFound) {
                List<Object> values = new ArrayList<>();
                for (String columnName : left.getColumns()) {
                    values.add(null);
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
                return resultRow.isNull(cn1.getColumnReference().getColumnName());
            case IS_NOT_NULL:
                ColumnNullPredicate cn2 = (ColumnNullPredicate) predicate;
                return !resultRow.isNull(cn2.getColumnReference().getColumnName());
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
            leftValue = (Comparable) resultRow.getObject(cr.getColumnName());
            rightValue = ((ColumnValuePredicate) sc).getValue();
        }

        if (sc instanceof ColumnColumnPredicate) {
            cr = ((ColumnColumnPredicate) sc).getLeftColumn();
            leftValue = (Comparable) resultRow.getObject(cr.getColumnName());
            ColumnReference cr2 = ((ColumnColumnPredicate) sc).getRightColumn();
            rightValue = (Comparable) resultRow.getObject(cr2.getColumnName());
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
        Table table = (Table) this.getTable((BaseTableReference) tableReference);
        return table.selectAll();
    }

    protected ResultSet selectFromJoinedTable(JoinTableReference tableReference)
            throws NoSuchTableException, NoSuchDatabaseException, NoSuchColumnException, WrongValueTypeException {


        ResultSet left = this.selectAll(tableReference.getLeftTableReference());
        ResultSet right = this.selectAll(tableReference.getRightTableReference());
        switch (tableReference.getType()) {
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


        logger.select(selectExpression);

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
        ResultSet resultSet = this.joinResultSets(resultSets, selectExpression.getPredicate());
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
                return (Table) database.getTable(tableReference.getTableName());
            }
        }
        throw new NoSuchDatabaseException(tableReference.getDatabaseName());
    }


//    public void checkConstraints(Table table, Object value)
//            throws WrongValueTypeException, ConstraintException {
//
//        TableMetadata tableMetadata = table.getMetadata();
//        Database database = table.getDatabase();
//        String tableName = tableMetadata.getName();
//        String dbName = database.getName();
//
//        ColumnReferenceImpl columnReference = new ColumnReferenceImpl(this.getName(), tableName, dbName);
//
//        if (value != null && !this.getJavaClass().isInstance(value)) {
//            throw new WrongValueTypeException(this.createColumnReference(this,
//                    tableMetadata.getName(), database.getName()),
//                    this.getJavaClass(), value.getClass());
//        }
//        if (value == null && this.isNotNull()) {
//            throw new ConstraintException(columnReference, "NOT NULL");
//        }
//        if (this.isPrimaryKey()) {
//            table.checkPrimaryKey(columnReference, value);
//        }
//    }

}