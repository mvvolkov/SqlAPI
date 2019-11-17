package serverLocalFileImpl;

import api.columnExpr.ColumnExpression;
import api.columnExpr.ColumnRef;
import api.connect.SqlServer;
import api.exceptions.*;
import api.metadata.TableMetadata;
import api.misc.SelectedItem;
import api.predicates.Predicate;
import api.queries.SelectExpression;
import api.queries.SqlStatement;
import api.selectResult.ResultRow;
import api.selectResult.ResultSet;
import api.tables.DatabaseTableReference;
import api.tables.JoinedTableReference;
import api.tables.TableFromSelectReference;
import api.tables.TableReference;
import org.jetbrains.annotations.NotNull;
import serverLocalFileImpl.persistent.PersistentColumnMetadata;
import serverLocalFileImpl.persistent.PersistentDatabase;
import serverLocalFileImpl.persistent.PersistentTable;
import sqlFactory.SqlManagerFactory;

import java.util.*;
import java.util.stream.Collectors;


public final class SqlServerImpl implements SqlServer {


    private final SqlServer logger =
            SqlManagerFactory.getServerLoggerSqlManager();

    private final Collection<PersistentDatabase> databases = new ArrayList<>();


    @Override
    public void createDatabase(String databaseName)
            throws DatabaseAlreadyExistsException {
        logger.createDatabase(databaseName);
        PersistentDatabase db = this.getDatabaseOrNull(databaseName);
        if (db != null) {
            throw new DatabaseAlreadyExistsException(databaseName);
        }
        databases.add(new PersistentDatabase(databaseName, this));

    }

    @Override
    public void openDatabaseWithTables(String dbName, List<TableMetadata> tables) {
    }


    @Override
    public void persistDatabase(String dbName) {
    }

    @Override public void executeStatement(SqlStatement statement) throws SqlException {
        logger.executeStatement(statement);
        this.getDatabase(statement.getDatabaseName()).executeStatement(statement);
    }

    @Override public ResultSet getQueryResult(SelectExpression selectExpression)
            throws SqlException {
        logger.getQueryResult(selectExpression);
        return createResultSet(this.getInternalQueryResult(selectExpression));
    }

    private PersistentDatabase getDatabase(String name) throws NoSuchDatabaseException {
        PersistentDatabase database = this.getDatabaseOrNull(name);
        if (database != null) {
            return database;
        }
        throw new NoSuchDatabaseException(name);
    }

    private PersistentDatabase getDatabaseOrNull(String name) {
        for (PersistentDatabase database : databases) {
            if (database.getName().equals(name)) {
                return database;
            }
        }
        return null;
    }

    /**
     * Conceptual Order of Evaluation of a Select Statement
     * 1.First the product of all tables in the from clause is formed.
     * 2.The where clause is then evaluated to eliminate rows that do not satisfy the search_condition.
     * 3.Next, the rows are grouped using the columns in the group by clause.
     * 4.Then, Groups that do not satisfy the search_condition in the having clause are eliminated.
     * 5.Next, the expressions in the select clause target list are evaluated.
     * 6.If the distinct keyword in present in the select clause, duplicate rows are now eliminated.
     * 7.The union is taken after each sub-select is evaluated.
     * 8.Finally, the resulting rows are sorted according to the columns specified in the order by clause.
     */
    public InternalResultSet getInternalQueryResult(SelectExpression se) throws
            SqlException {

        InternalResultSet result = this.getProductOfTables(se.getTableReferences());
        result = getFilteredResult(result, se.getPredicate());

        if (se.getSelectedItems().isEmpty()) {
            return result;
        }

        List<ColumnExpression> columnExpressions =
                this.getSelectedColumnExpressions(se.getSelectedItems());

        List<ColumnRef> resultColumns = getSelectedColumns(columnExpressions);
        List<InternalResultRow> resultRows;
        if (!se.getGroupByColumns().isEmpty()) {
            Collection<InternalResultGroup> groups =
                    getGroups(se.getGroupByColumns(), result.getRows());

            resultRows = new ArrayList<>();
            for (InternalResultGroup group : groups) {
                Map<ColumnRef, Object> values = new HashMap<>();
                for (ColumnExpression ce : columnExpressions) {
                    values.put(new ColumnRefImpl(ce.getAlias()),
                            group.evaluateColumnExpr(ce));
                }
                resultRows.add(new InternalResultRow(values));
            }
        } else {
            resultRows = getSelectedValues(result, resultColumns, columnExpressions);
        }
        return new InternalResultSet(resultColumns, resultRows);
    }

    private static Collection<InternalResultGroup> getGroups(
            List<ColumnRef> groupByColumns,
            Collection<InternalResultRow> rows)
            throws NoSuchColumnException, AmbiguousColumnNameException {
        Map<List<Object>, List<InternalResultRow>> map = new HashMap<>();
        for (InternalResultRow row : rows) {
            List<Object> values = new ArrayList<>();
            for (ColumnRef cr : groupByColumns) {
                values.add(row.evaluateColumnRef(cr));
            }
            List<Object> key = Collections.unmodifiableList(values);
            if (!map.containsKey(key)) {
                map.put(key, new ArrayList<>());
            }
            map.get(key).add(row);
        }
        Collection<InternalResultGroup> groups = new ArrayList<>();
        for (Map.Entry<List<Object>, List<InternalResultRow>> entry : map.entrySet()) {
            groups.add(new InternalResultGroup(groupByColumns, entry.getKey(),
                    entry.getValue()));
        }
        return groups;
    }

    private List<ColumnExpression> getSelectedColumnExpressions(
            List<SelectedItem> selectedItems)
            throws NoSuchTableException, NoSuchSchemaException, NoSuchDatabaseException {

        List<ColumnExpression> columnExpressions = new ArrayList<>();
        for (SelectedItem selectedItem : selectedItems) {
            if (selectedItem instanceof DatabaseTableReference) {
                String tableName =
                        ((DatabaseTableReference) selectedItem).getTableName();
                String schemaName =
                        ((DatabaseTableReference) selectedItem).getSchemaName();
                List<PersistentColumnMetadata> columns =
                        this.getTable((DatabaseTableReference) selectedItem).getColumns();
                for (PersistentColumnMetadata column : columns) {
                    columnExpressions.add(new ColumnRefImpl(schemaName, tableName,
                            column.getColumnName()));
                }
            }
            if (selectedItem instanceof ColumnExpression) {
                columnExpressions.add((ColumnExpression) selectedItem);
            }
        }
        return columnExpressions;

    }

    private static List<ColumnRef> getSelectedColumns(
            List<ColumnExpression> columnExpressions) {

        List<ColumnRef> resultColumns = new ArrayList<>();
        for (ColumnExpression columnExpression : columnExpressions) {
            if (!columnExpression.getAlias().isEmpty()) {
                resultColumns.add(new ColumnRefImpl((columnExpression).getAlias()));
            } else if (columnExpression instanceof ColumnRef) {
                resultColumns.add(new ColumnRefImpl((ColumnRef) columnExpression));
            } else {
                resultColumns.add(new ColumnRefImpl(columnExpression.toString()));
            }
        }
        return resultColumns;
    }

    private static List<InternalResultRow> getSelectedValues(
            InternalResultSet internalResultSet, List<ColumnRef> columns,
            List<ColumnExpression> columnExpressions)
            throws NoSuchColumnException, AmbiguousColumnNameException {

        List<InternalResultRow> resultRows = new ArrayList<>();
        for (InternalResultRow row : internalResultSet.getRows()) {
            Map<ColumnRef, Object> values = new HashMap<>();
            for (int i = 0; i < columns.size(); i++) {
                values.put(columns.get(i),
                        row.evaluateColumnExpr(columnExpressions.get(i)));
            }
            resultRows.add(new InternalResultRow(values));
        }
        return resultRows;
    }


    public static ResultSetImpl createResultSet(InternalResultSet internalResultSet) {

        List<ResultRow> resultRows = new ArrayList<>();
        List<String> resultColumns = internalResultSet.getColumns().stream()
                .map(ColumnRef::getColumnName).collect(Collectors.toList());

        for (InternalResultRow row : internalResultSet.getRows()) {
            Map<String, Object> values = new HashMap<>();
            for (Map.Entry<ColumnRef, Object> entry : row.getValues().entrySet()) {
                values.put(entry.getKey().getColumnName(), entry.getValue());
            }
            resultRows.add(new ResultRowImpl(values));
        }
        return new ResultSetImpl(resultRows, resultColumns);
    }

    private InternalResultSet getProductOfTables(List<TableReference> tableReferences)
            throws SqlException {

        List<InternalResultSet> resultSets = new ArrayList<>();
        for (TableReference tr : tableReferences) {
            resultSets.add(this.getDataFromTableRef(tr));
        }
        if (resultSets.isEmpty()) {
            return new InternalResultSet(Collections.emptyList(),
                    Collections.emptyList());
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
            throws NoSuchColumnException, WrongValueTypeException,
            AmbiguousColumnNameException {
        List<ColumnRef> columns = new ArrayList<>(resultSet.getColumns());
        List<InternalResultRow> rows = new ArrayList<>();
        for (InternalResultRow row : resultSet.getRows()) {
            if (row.matchPredicate(predicate)) {
                rows.add(row);
            }
        }
        return new InternalResultSet(columns, rows);
    }

    private static InternalResultSet innerJoin(InternalResultSet left,
                                               InternalResultSet right, Predicate sc)
            throws NoSuchColumnException, WrongValueTypeException,
            AmbiguousColumnNameException {

        InternalResultSet internalResultSet = left.joinWith(right);
        return getFilteredResult(internalResultSet, sc);
    }

    private static InternalResultSet leftOuterJoin(InternalResultSet left,
                                                   InternalResultSet right, Predicate sc)
            throws NoSuchColumnException, WrongValueTypeException,
            AmbiguousColumnNameException {

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
                if (row.matchPredicate(sc)) {
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

    private static InternalResultSet rightOuterJoin(InternalResultSet left,
                                                    InternalResultSet right, Predicate sc)
            throws NoSuchColumnException, WrongValueTypeException,
            AmbiguousColumnNameException {

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
                if (row.matchPredicate(sc)) {
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


    private InternalResultSet getDataFromDatabaseTable(DatabaseTableReference dtr)
            throws SqlException {
        return this.getTable(dtr).getData();
    }

    private InternalResultSet getDataFromJoinedTable(JoinedTableReference tableReference)
            throws SqlException {


        InternalResultSet left =
                this.getDataFromTableRef(tableReference.getLeftTableReference());
        InternalResultSet right =
                this.getDataFromTableRef(tableReference.getRightTableReference());
        switch (tableReference.getTableRefType()) {
            case INNER_JOIN:
                return innerJoin(left, right, tableReference.getPredicate());
            case LEFT_OUTER_JOIN:
                return leftOuterJoin(left, right, tableReference.getPredicate());
            case RIGHT_OUTER_JOIN:
                return rightOuterJoin(left, right, tableReference.getPredicate());
        }
        throw new InvalidQueryException("Invalid type of join table reference");
    }

    private InternalResultSet getSubqueryResult(TableFromSelectReference tr)
            throws SqlException {
        InternalResultSet internalResultSet =
                getInternalQueryResult(tr.getSelectExpression());
        String alias = tr.getAlias();
        if (alias.isEmpty()) {
            return internalResultSet;
        }
        List<ColumnRef> newColumns = new ArrayList<>();
        for (ColumnRef cr : internalResultSet.getColumns()) {
            newColumns.add(new ColumnRefImpl("", alias, cr.getColumnName()));
        }
        List<InternalResultRow> newRows = new ArrayList<>();
        for (InternalResultRow row : internalResultSet.getRows()) {
            Map<ColumnRef, Object> values = new HashMap<>();
            for (Map.Entry<ColumnRef, Object> entry : row.getValues().entrySet()) {
                values.put(new ColumnRefImpl("", alias, entry.getKey().getColumnName()),
                        entry.getValue());
            }
            newRows.add(new InternalResultRow(values));
        }
        return new InternalResultSet(newColumns, newRows);
    }


    private @NotNull InternalResultSet getDataFromTableRef(
            TableReference tableReference) throws
            SqlException {

        switch (tableReference.getTableRefType()) {
            case DATABASE_TABLE:
                return this.getDataFromDatabaseTable(
                        (DatabaseTableReference) tableReference);
            case INNER_JOIN:
            case LEFT_OUTER_JOIN:
            case RIGHT_OUTER_JOIN:
                return this.getDataFromJoinedTable((JoinedTableReference) tableReference);
            case SELECT_SUBQUERY:
                return this.getSubqueryResult(
                        (TableFromSelectReference) tableReference);

        }
        throw new IllegalArgumentException("Invalid type of table reference.");
    }

    private PersistentTable getTable(DatabaseTableReference tableReference) throws
            NoSuchTableException, NoSuchSchemaException, NoSuchDatabaseException {
        return this.getDatabase(tableReference.getDatabaseName())
                .getTable(tableReference.getSchemaName(), tableReference.getTableName());
    }


}
