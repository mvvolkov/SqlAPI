package serverLocalFileImpl;

import api.*;
import api.columnExpr.BinaryColumnExpression;
import api.columnExpr.ColumnExpression;
import api.columnExpr.ColumnRef;
import api.columnExpr.ColumnValue;
import api.exceptions.*;
import api.metadata.TableMetadata;
import api.predicates.*;
import api.queries.*;
import org.jetbrains.annotations.NotNull;
import serverLoggerImpl.SqlServerLoggerImpl;

import java.util.*;

public final class SqlServerImpl implements SqlServer {


    private final transient SqlServer logger = new SqlServerLoggerImpl();

    private final Collection<Database> databases = new ArrayList<>();


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
                this.delete((DeleteStatement) stmt);
                return;
            case UPDATE:
                this.update((UpdateStatement) stmt);
                return;
        }
    }


    private void createTable(CreateTableStatement stmt)
            throws TableAlreadyExistsException, NoSuchDatabaseException {
        Database database = this.getDatabase(stmt.getDatabaseName());
        Table table = database.getTableOrNull(stmt.getTableName());
        if (table != null) {
            throw new TableAlreadyExistsException(database.getName(),
                    stmt.getTableName());
        }
        database.addTable(
                new Table(database.getName(), stmt.getTableName(), stmt.getColumns()));
    }

    private void insert(InsertStatement stmt)
            throws NoSuchDatabaseException, NoSuchTableException, ConstraintException,
            WrongValueTypeException {
        Table table = this.getTableForStatement(stmt);
        if (stmt.getColumns() == null) {
            table.insert(stmt.getValues());
        } else {
            table.insert(stmt.getColumns(), stmt.getValues());
        }
    }

    private Table getTableForStatement(SqlStatement stmt)
            throws NoSuchDatabaseException, NoSuchTableException {
        Database database = this.getDatabase(stmt.getDatabaseName());
        return database.getTable(stmt.getTableName());
    }

    private void delete(DeleteStatement stmt)
            throws NoSuchDatabaseException, NoSuchTableException {
        Table table = this.getTableForStatement(stmt);
        table.delete(stmt);
    }

    private void update(UpdateStatement stmt)
            throws NoSuchDatabaseException, NoSuchTableException {
        Table table = this.getTableForStatement(stmt);
        table.update(stmt);
    }


    private Database getDatabaseOrNull(String dbName) {
        for (Database database : databases) {
            if (database.getName().equals(dbName)) {
                return database;
            }
        }
        return null;
    }


    private @NotNull Database getDatabase(String dbName) throws NoSuchDatabaseException {
        Database database = this.getDatabaseOrNull(dbName);
        if (database == null) {
            throw new NoSuchDatabaseException(dbName);
        }
        return database;
    }

    private static List<Value> getValuesFromResultSets(List<InternalResultSet> resultSets,
                                                       Deque<Integer> stack) {
        Deque<Integer> rowNumbers = new ArrayDeque<>(stack);
        List<Value> values = new ArrayList<>();
        while (!rowNumbers.isEmpty()) {
            int rowNumber = rowNumbers.pop();
            InternalResultSet resultSet = resultSets.get(rowNumbers.size());
            InternalResultRow row = resultSet.getRows().get(rowNumber);
            values.addAll(0, row.getValues());
        }
        return values;
    }

    private static InternalResultSet joinResultSets(List<InternalResultSet> resultSets,
                                                    Predicate selectionPredicate)
            throws NoSuchColumnException, WrongValueTypeException {

        // Create full list of columns.
        List<ColumnRef> columns = new ArrayList<>();
        boolean hasEmptyResultSet = false;
        for (InternalResultSet resultSet : resultSets) {
            columns.addAll(resultSet.getColumns());
            if (resultSet.getRows().isEmpty()) {
                hasEmptyResultSet = true;
            }
        }
        if (hasEmptyResultSet) {
            return new InternalResultSet(columns, Collections.EMPTY_LIST);
        }

        if (resultSets.size() == 1) {
            InternalResultSet resultSet = resultSets.get(0);
            List<InternalResultRow> rows = new ArrayList<>();
            for (InternalResultRow row : resultSet.getRows()) {
                if (evaluate(row, selectionPredicate)) {
                    rows.add(row);
                }
            }
            return new InternalResultSet(columns, rows);
        }

        // Create all possible combinations of rows from different result sets.
        List<InternalResultRow> rows = new ArrayList<>();
        // Use stack here to get all combinations.
        Deque<Integer> rowNumbers = new ArrayDeque<>(resultSets.size());
        rowNumbers.push(0);
        boolean nextResultSetHasNoRows = false;
        while (!rowNumbers.isEmpty()) {
            if (rowNumbers.size() == resultSets.size() || nextResultSetHasNoRows) {
                int rowNumber = rowNumbers.pop();
                nextResultSetHasNoRows = (rowNumber ==
                        resultSets.get(rowNumbers.size()).getRows().size() - 1);
                if (nextResultSetHasNoRows) {
                    continue;
                }
                rowNumbers.push(++rowNumber);
            }
            while (rowNumbers.size() < resultSets.size()) {
                rowNumbers.push(0);
            }
            InternalResultRow row = new InternalResultRow(columns,
                    getValuesFromResultSets(resultSets, rowNumbers));
            if (evaluate(row, selectionPredicate)) {
                rows.add(row);
            }
        }
        return new InternalResultSet(columns, rows);
    }

    private static InternalResultSet innerJoin(InternalResultSet left,
                                               InternalResultSet right, Predicate sc)
            throws NoSuchColumnException, WrongValueTypeException {

        List<ColumnRef> columns = new ArrayList<>();
        columns.addAll(left.getColumns());
        columns.addAll(right.getColumns());
        List<InternalResultRow> rows = new ArrayList<>();
        for (InternalResultRow leftRow : left.getRows()) {
            for (InternalResultRow rightRow : right.getRows()) {
                List<Value> values = new ArrayList<>();
                values.addAll(leftRow.getValues());
                values.addAll(rightRow.getValues());
                InternalResultRow row = new InternalResultRow(columns, values);
                if (evaluate(row, sc)) {
                    rows.add(row);
                }
            }
        }
        return new InternalResultSet(columns, rows);
    }

    private static InternalResultSet leftOutJoin(InternalResultSet left,
                                                 InternalResultSet right, Predicate sc)
            throws NoSuchColumnException, WrongValueTypeException {

        List<ColumnRef> columns = new ArrayList<>();
        columns.addAll(left.getColumns());
        columns.addAll(right.getColumns());
        List<InternalResultRow> rows = new ArrayList<>();
        for (InternalResultRow leftRow : left.getRows()) {
            boolean matchFound = false;
            for (InternalResultRow rightRow : right.getRows()) {
                List<Value> values = new ArrayList<>();
                values.addAll(leftRow.getValues());
                values.addAll(rightRow.getValues());
                InternalResultRow row = new InternalResultRow(columns, values);
                if (evaluate(row, sc)) {
                    rows.add(row);
                    matchFound = true;
                }
            }
            if (!matchFound) {
                List<Value> values = new ArrayList<>();
                values.addAll(leftRow.getValues());
                for (ColumnRef column : right.getColumns()) {
                    values.add(new Value(null, null));
                }
            }
        }
        return new InternalResultSet(columns, rows);
    }

    private static InternalResultSet rightOutJoin(InternalResultSet left,
                                                  InternalResultSet right, Predicate sc)
            throws NoSuchColumnException, WrongValueTypeException {

        List<ColumnRef> columns = new ArrayList<>();
        columns.addAll(left.getColumns());
        columns.addAll(right.getColumns());
        List<InternalResultRow> rows = new ArrayList<>();
        for (InternalResultRow rightRow : right.getRows()) {
            boolean matchFound = false;
            for (InternalResultRow leftRow : left.getRows()) {
                List<Value> values = new ArrayList<>();
                values.addAll(leftRow.getValues());
                values.addAll(rightRow.getValues());
                InternalResultRow row = new InternalResultRow(columns, values);
                if (evaluate(row, sc)) {
                    rows.add(row);
                    matchFound = true;
                }
            }
            if (!matchFound) {
                List<Object> values = new ArrayList<>();
                for (ColumnRef column : left.getColumns()) {
                    values.add(new Value(null, null));
                }
                values.addAll(rightRow.getValues());
            }
        }
        return new InternalResultSet(columns, rows);
    }

    static boolean evaluate(InternalResultRow resultRow, Predicate predicate)
            throws NoSuchColumnException, WrongValueTypeException {

        if (predicate.getType() == Predicate.Type.TRUE) {
            return true;
        } else if (predicate.getType() == Predicate.Type.FALSE) {
            return false;
        }
        if (predicate instanceof CombinedPredicate) {
            switch (predicate.getType()) {
                case AND:
                    CombinedPredicate cp1 = (CombinedPredicate) predicate;
                    return evaluate(resultRow, cp1.getLeftPredicate()) &&
                            evaluate(resultRow, cp1.getRightPredicate());
                case OR:
                    CombinedPredicate cp2 = (CombinedPredicate) predicate;
                    return evaluate(resultRow, cp2.getLeftPredicate()) ||
                            evaluate(resultRow, cp2.getRightPredicate());
            }
        }
        if (predicate instanceof BinaryPredicate) {
            return compareValues(resultRow, (BinaryPredicate) predicate);
        }
        return false;
    }

    static boolean compareValues(InternalResultRow resultRow, BinaryPredicate predicate)
            throws WrongValueTypeException, NoSuchColumnException {


        Comparable leftValue =
                (Comparable) evaluateColumnExpr(resultRow, predicate.getLeftOperand());
        Comparable rightValue =
                (Comparable) evaluateColumnExpr(resultRow, predicate.getRightOperand());


        if (leftValue == null || rightValue == null) {
            return false;
        }

//        if (!leftValue.getClass().equals(rightValue.getClass())) {
//            throw new WrongValueTypeException(cr, leftValue.getClass(),
//                    rightValue.getClass());
//        }


        int compResult = leftValue.compareTo(rightValue);
        switch (predicate.getType()) {
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

    protected static Object evaluateColumnExpr(InternalResultRow row,
                                               ColumnExpression ce) {

        if (ce instanceof BinaryColumnExpression) {
            return evaluateBinaryColumnExpr(row, (BinaryColumnExpression) ce);
        }
        if (ce instanceof ColumnRef) {
            return evaluateColumnRef(row, (ColumnRef) ce);
        }
        if (ce instanceof ColumnValue) {
            return ((ColumnValue) ce).getValue();
        }
        return null;
    }

    protected static Object evaluateBinaryColumnExpr(InternalResultRow row,
                                                     BinaryColumnExpression bce) {
        Object leftValue = evaluateColumnExpr(row, bce.getLeftOperand());
        Object rightValue = evaluateColumnExpr(row, bce.getRightOperand());
        switch (bce.getType()) {
            case ADD:
                if (leftValue instanceof Integer && rightValue instanceof Integer) {
                    return (Integer) (((Integer) leftValue) + ((Integer) rightValue));
                }
                if (leftValue instanceof String && rightValue instanceof String) {
                    return (String) (((String) leftValue) + ((String) rightValue));
                }
            case SUBTRACT:
                if (leftValue instanceof Integer && rightValue instanceof Integer) {
                    return (Integer) (((Integer) leftValue) - ((Integer) rightValue));
                }
            case MULTIPLY:
                if (leftValue instanceof Integer && rightValue instanceof Integer) {
                    return (Integer) (((Integer) leftValue) * ((Integer) rightValue));
                }
            case DIVIDE:
                if (leftValue instanceof Integer && rightValue instanceof Integer) {
                    return (Integer) (((Integer) leftValue) / ((Integer) rightValue));
                }
        }
        return null;
    }

    protected static Object evaluateColumnRef(InternalResultRow row, ColumnRef cr) {

        for (int i = 0; i < row.getColumns().size(); i++) {
            ColumnRef cr1 = row.getColumns().get(i);
            if (cr1.getDatabaseName().equals(cr.getDatabaseName()) &&
                    cr1.getTableName().equals(cr.getTableName())
                    && cr1.getColumnName().equals(cr.getColumnName())) {
                return row.getValues().get(i).getValue();
            }
        }
        return null;
    }


    protected InternalResultSet selectFromBaseTable(DatabaseTableReference dtr)
            throws SqlException {
        return this.getTable(dtr).getData();
    }

    protected InternalResultSet selectFromJoinedTable(JoinTableReference tableReference)
            throws SqlException {


        InternalResultSet left = this.selectAll(tableReference.getLeftTableReference());
        InternalResultSet right = this.selectAll(tableReference.getRightTableReference());
        switch (tableReference.getType()) {
            case INNER_JOIN:
                return innerJoin(left, right, tableReference.getSelectionPredicate());
            case LEFT_OUTER_JOIN:
                return leftOutJoin(left, right, tableReference.getSelectionPredicate());
            case RIGHT_OUTER_JOIN:
                return rightOutJoin(left, right, tableReference.getSelectionPredicate());
        }
        return new InternalResultSet(Collections.EMPTY_LIST, Collections.EMPTY_LIST);
    }

    @Override
    public @NotNull ResultSet select(SelectExpression selectExpression) throws
            SqlException {


        logger.select(selectExpression);

        List<InternalResultSet> resultSets = new ArrayList<>();
        for (TableReference tr : selectExpression.getTableReferences()) {
            if (tr instanceof DatabaseTableReference) {
                resultSets.add(this.selectFromBaseTable((DatabaseTableReference) tr));
                continue;
            } else if (tr instanceof JoinTableReference) {
                resultSets.add(this.selectFromJoinedTable((JoinTableReference) tr));
                continue;
            }
//            else if (tr instanceof SelectExpression) {
//                resultSets.add(this.select((SelectExpression) tr));
//            }
        }
        InternalResultSet internalResultSet =
                this.joinResultSets(resultSets, selectExpression.getPredicate());

        List<ResultRow> resultRows = new ArrayList<>();
        List<String> resultColumns = new ArrayList<>();
        for (ColumnRef columnRef : internalResultSet.getColumns()) {
            resultColumns.add(columnRef.getColumnName());
        }
        for (InternalResultRow row : internalResultSet.getRows()) {
            List<Object> values = new ArrayList<>();
            for (Value value : row.getValues()) {
                values.add(value.getValue());
            }
            resultRows.add(new ResultRowImpl(resultColumns, values));
        }
        return new ResultSetImpl(resultRows, resultColumns);
    }

    protected @NotNull InternalResultSet selectAll(TableReference tableReference) throws
            SqlException {

        if (tableReference instanceof DatabaseTableReference) {
            return this.selectFromBaseTable((DatabaseTableReference) tableReference);
        } else if (tableReference instanceof JoinTableReference) {
            return this.selectFromJoinedTable((JoinTableReference) tableReference);
        }
//        else if (tableReference instanceof SelectExpression) {
//            return this.select((SelectExpression) tableReference);
//        }
        throw new IllegalArgumentException("");
    }

    protected Table getTable(DatabaseTableReference tableReference) throws
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
