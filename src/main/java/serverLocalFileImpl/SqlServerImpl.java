package serverLocalFileImpl;

import api.*;
import api.columnExpr.ColumnExpression;
import api.columnExpr.ColumnRef;
import api.exceptions.*;
import api.metadata.TableMetadata;
import api.predicates.Predicate;
import api.queries.*;
import api.SelectedItem;
import api.selectResult.ResultRow;
import api.selectResult.ResultSet;
import api.tables.DatabaseTableReference;
import api.tables.JoinTableReference;
import api.tables.TableReference;
import org.jetbrains.annotations.NotNull;
import serverLocalFileImpl.persistent.PersistentDatabase;
import serverLocalFileImpl.persistent.PersistentTable;
import serverLoggerImpl.SqlServerLoggerImpl;
import sqlFactory.SqlManagerFactory;

import java.util.*;

public final class SqlServerImpl implements SqlServer {


    private final transient SqlServer logger =
            SqlManagerFactory.getServerLoggerSqlManager();

    private final Collection<PersistentDatabase> databases = new ArrayList<>();


    @Override
    public void createDatabase(String dbName) throws DatabaseAlreadyExistsException {
        logger.createDatabase(dbName);
        for (PersistentDatabase database : databases) {
            if (database.getName().equals(dbName)) {
                throw new DatabaseAlreadyExistsException(dbName);
            }
        }
        databases.add(new PersistentDatabase(dbName));
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
            case INSERT_FROM_SELECT:
                this.insert((InsertFromSelectStatement) stmt);
                return;
            case DELETE:
                this.delete((DeleteStatement) stmt);
                return;
            case UPDATE:
                this.update((UpdateStatement) stmt);
                return;
        }
        throw new InvalidQueryException("Invalid type of SQL query.");
    }

    @Override
    public @NotNull ResultSet select(SelectExpression se) throws
            SqlException {

        logger.select(se);
        List<InternalResultSet> resultSets = new ArrayList<>();
        for (TableReference tr : se.getTableReferences()) {
            resultSets.add(this.getDataFromTableRef(tr));
        }
        InternalResultSet internalResultSet = getJoinedResult(resultSets);
        internalResultSet = getFilteredResult(internalResultSet, se.getPredicate());
        return createResultSet(internalResultSet, se.getSelectedItems());
    }

    private static ResultSetImpl createResultSet(InternalResultSet internalResultSet,
                                                 List<SelectedItem> selectedItems) {

        List<ResultRow> resultRows = new ArrayList<>();
        List<String> resultColumns = new ArrayList<>();

        if (selectedItems.isEmpty()) {
            for (ColumnRef cr : internalResultSet.getColumns()) {
                resultColumns.add(cr.getColumnName());
            }
        } else {
            for (SelectedItem selectedItem : selectedItems) {
                if (selectedItem instanceof DatabaseTableReference) {
                    String tableName =
                            ((DatabaseTableReference) selectedItem).getTableName();
                    String dbName =
                            ((DatabaseTableReference) selectedItem).getDatabaseName();
                    for (ColumnRef cr : internalResultSet.getColumns()) {
                        if (cr.getDatabaseName().equals(dbName) &&
                                cr.getTableName().equals(tableName)) {
                            resultColumns.add(cr.getColumnName());
                        }
                    }
                }
                if (selectedItem instanceof ColumnExpression) {
                    resultColumns
                            .add(((ColumnExpression) selectedItem).getAlias());
                }
            }
        }


        for (InternalResultRow row : internalResultSet.getRows()) {
            List<Object> values = new ArrayList<>();

            if (selectedItems.isEmpty()) {
                for (ColumnRef cr : internalResultSet.getColumns()) {
                    values.add(row.getValues().get(cr));
                }
            } else {
                for (SelectedItem selectedItem : selectedItems) {
                    if (selectedItem instanceof DatabaseTableReference) {
                        String tableName =
                                ((DatabaseTableReference) selectedItem).getTableName();
                        String dbName =
                                ((DatabaseTableReference) selectedItem).getDatabaseName();
                        for (ColumnRef cr : internalResultSet.getColumns()) {
                            if (cr.getDatabaseName().equals(dbName) &&
                                    cr.getTableName().equals(tableName)) {
                                values.add(row.getValues().get(cr));
                            }
                        }
                    }
                    if (selectedItem instanceof ColumnExpression) {
                        values.add(PredicateHelper
                                .evaluateColumnExpr(row,
                                        (ColumnExpression) selectedItem));
                    }
                }
            }
            resultRows.add(new ResultRowImpl(resultColumns, values));
        }

        return new ResultSetImpl(resultRows, resultColumns);
    }


    private void createTable(CreateTableStatement stmt)
            throws TableAlreadyExistsException, NoSuchDatabaseException {
        PersistentDatabase database = this.getDatabase(stmt.getDatabaseName());
        PersistentTable table = database.getTableOrNull(stmt.getTableName());
        if (table != null) {
            throw new TableAlreadyExistsException(database.getName(),
                    stmt.getTableName());
        }
        database.addTable(
                new PersistentTable(database.getName(), stmt.getTableName(),
                        stmt.getColumns()));
    }

    private void insert(InsertStatement stmt)
            throws SqlException {
        this.getTable(stmt).insert(stmt.getColumns(), stmt.getValues());
    }

    private void insert(InsertFromSelectStatement stmt)
            throws SqlException {
        ResultSet resultSet = this.select(stmt.getSelectExpression());
        for (ResultRow row : resultSet.getRows()) {
            this.getTable(stmt).insert(stmt.getColumns(), row.getValues());
        }
    }


    private void delete(DeleteStatement stmt)
            throws NoSuchDatabaseException, NoSuchTableException {
        this.getTable(stmt).delete(stmt.getPredicate());
    }

    private void update(UpdateStatement stmt)
            throws NoSuchDatabaseException, NoSuchTableException {
        PersistentTable table = this.getTable(stmt);
        table.update(stmt);
    }

    private PersistentTable getTable(SqlStatement stmt)
            throws NoSuchDatabaseException, NoSuchTableException {
        PersistentDatabase database = this.getDatabase(stmt.getDatabaseName());
        return database.getTable(stmt.getTableName());
    }


    private @NotNull PersistentDatabase getDatabase(String dbName)
            throws NoSuchDatabaseException {
        for (PersistentDatabase database : databases) {
            if (database.getName().equals(dbName)) {
                return database;
            }
        }
        throw new NoSuchDatabaseException(dbName);
    }


    private static InternalResultSet getJoinedResult(List<InternalResultSet> resultSets)
            throws NoSuchColumnException, WrongValueTypeException {

        if (resultSets.isEmpty()) {
            return new InternalResultSet(Collections.EMPTY_LIST, Collections.EMPTY_LIST);
        }
        Iterator<InternalResultSet> it = resultSets.iterator();
        InternalResultSet resultSet = it.next();
        while (it.hasNext()) {
            resultSet = resultSet.joinWith(it.next());
        }
        return resultSet;
    }

    private static InternalResultSet getFilteredResult(InternalResultSet resultSet,
                                                       Predicate predicate)
            throws NoSuchColumnException, WrongValueTypeException {
        List<ColumnRef> columns = new ArrayList<>(resultSet.getColumns());
        List<InternalResultRow> rows = new ArrayList<>();
        for (InternalResultRow row : resultSet.getRows()) {
            if (PredicateHelper.matchRow(row, predicate)) {
                rows.add(row);
            }
        }
        return new InternalResultSet(columns, rows);
    }

    private static InternalResultSet innerJoin(InternalResultSet left,
                                               InternalResultSet right, Predicate sc)
            throws NoSuchColumnException, WrongValueTypeException {

        InternalResultSet internalResultSet = left.joinWith(right);
        return getFilteredResult(internalResultSet, sc);
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
                Map<ColumnRef, Object> values = new HashMap<>(leftRow.getValues());
                values.putAll(rightRow.getValues());
                InternalResultRow row = new InternalResultRow(values);
                if (PredicateHelper.matchRow(row, sc)) {
                    rows.add(row);
                    matchFound = true;
                }
            }
            if (!matchFound) {
                Map<ColumnRef, Object> values = new HashMap<>(leftRow.getValues());
                rows.add(new InternalResultRow(values));
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
                Map<ColumnRef, Object> values = new HashMap<>(leftRow.getValues());
                values.putAll(rightRow.getValues());
                InternalResultRow row = new InternalResultRow(values);
                if (PredicateHelper.matchRow(row, sc)) {
                    rows.add(row);
                    matchFound = true;
                }
            }
            if (!matchFound) {
                Map<ColumnRef, Object> values = new HashMap<>(rightRow.getValues());
                rows.add(new InternalResultRow(values));
            }
        }
        return new InternalResultSet(columns, rows);
    }


    protected InternalResultSet getDataFromDatabaseTable(DatabaseTableReference dtr)
            throws SqlException {
        return this.getTable(dtr).getData();
    }

    protected InternalResultSet getDataFromJoinedTable(JoinTableReference tableReference)
            throws SqlException {


        InternalResultSet left =
                this.getDataFromTableRef(tableReference.getLeftTableReference());
        InternalResultSet right =
                this.getDataFromTableRef(tableReference.getRightTableReference());
        switch (tableReference.getTableRefType()) {
            case INNER_JOIN:
                return innerJoin(left, right, tableReference.getPredicate());
            case LEFT_OUTER_JOIN:
                return leftOutJoin(left, right, tableReference.getPredicate());
            case RIGHT_OUTER_JOIN:
                return rightOutJoin(left, right, tableReference.getPredicate());
        }
        throw new InvalidQueryException("Invalid type of join table reference");
    }


    protected @NotNull InternalResultSet getDataFromTableRef(
            TableReference tableReference) throws
            SqlException {

        if (tableReference instanceof DatabaseTableReference) {
            return this.getDataFromDatabaseTable((DatabaseTableReference) tableReference);
        } else if (tableReference instanceof JoinTableReference) {
            return this.getDataFromJoinedTable((JoinTableReference) tableReference);
        }
//        else if (tableReference instanceof SelectExpression) {
//            return this.select((SelectExpression) tableReference);
//        }
        throw new IllegalArgumentException("");
    }

    protected PersistentTable getTable(DatabaseTableReference tableReference) throws
            NoSuchTableException, NoSuchDatabaseException {
        for (PersistentDatabase database : databases) {
            if (database.getName().equals(tableReference.getDatabaseName())) {
                return (PersistentTable) database.getTable(tableReference.getTableName());
            }
        }
        throw new NoSuchDatabaseException(tableReference.getDatabaseName());
    }


}
