package serverLocalFileImpl;

import api.misc.SelectedItem;
import api.connect.SqlServer;
import api.columnExpr.ColumnExpression;
import api.columnExpr.ColumnRef;
import api.exceptions.*;
import api.metadata.TableMetadata;
import api.predicates.Predicate;
import api.queries.*;
import api.selectResult.ResultRow;
import api.selectResult.ResultSet;
import api.tables.DatabaseTableReference;
import api.tables.JoinedTableReference;
import api.tables.TableFromSelectReference;
import api.tables.TableReference;
import org.jetbrains.annotations.NotNull;
import serverLocalFileImpl.persistent.PersistentSchema;
import serverLocalFileImpl.persistent.PersistentTable;
import sqlFactory.SqlManagerFactory;

import java.util.*;
import java.util.stream.Collectors;


public final class SqlServerImpl implements SqlServer {


    private final transient SqlServer logger =
            SqlManagerFactory.getServerLoggerSqlManager();

    private final Collection<PersistentSchema> schemas = new ArrayList<>();

    private final PersistentSchema defaultSchema;

    public SqlServerImpl() {
        defaultSchema = new PersistentSchema("dbo");
        schemas.add(defaultSchema);
    }

    private PersistentSchema getCurrentSchema() {
        return defaultSchema;
    }


    @Override
    public void createDatabase(String schemaName) throws DatabaseAlreadyExistsException {
        logger.createDatabase(schemaName);
        for (PersistentSchema schema : schemas) {
            if (schema.getName().equals(schemaName)) {
                throw new DatabaseAlreadyExistsException(schemaName);
            }
        }
        schemas.add(new PersistentSchema(schemaName));
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
    public @NotNull ResultSet getQueryResult(SelectExpression se) throws
            SqlException {
        logger.getQueryResult(se);
        return createResultSet(this.getInternalQueryResult(se));
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


    private InternalResultSet getInternalQueryResult(SelectExpression se) throws
            SqlException {

        List<InternalResultSet> resultSets = new ArrayList<>();
        for (TableReference tr : se.getTableReferences()) {
            resultSets.add(this.getDataFromTableRef(tr));
        }
        InternalResultSet internalResultSet = getProductOfResults(resultSets);
        internalResultSet = getFilteredResult(internalResultSet, se.getPredicate());
        internalResultSet = getSelectedColumns(internalResultSet, se.getSelectedItems());
        return internalResultSet;
    }

    private static InternalResultSet getSelectedColumns(InternalResultSet internalResultSet,
                                                        List<SelectedItem> selectedItems) throws NoSuchColumnException, AmbiguousColumnNameException {
        List<InternalResultRow> resultRows = new ArrayList<>();
        List<ColumnRef> resultColumns = new ArrayList<>();

        if (selectedItems.isEmpty()) {
            return internalResultSet;
        }

        for (SelectedItem selectedItem : selectedItems) {
            if (selectedItem instanceof DatabaseTableReference) {
                String tableName =
                        ((DatabaseTableReference) selectedItem).getTableName();
                String schemaName =
                        ((DatabaseTableReference) selectedItem).getSchemaName();
                for (ColumnRef cr : internalResultSet.getColumns()) {
                    if (cr.getSchemaName().equals(schemaName) &&
                            cr.getTableName().equals(tableName)) {
                        resultColumns.add(cr);
                    }
                }
            }
            if (selectedItem instanceof ColumnExpression) {
                resultColumns.add(new ColumnRefImpl(((ColumnExpression) selectedItem).getAlias()));
            }
        }
        for (InternalResultRow row : internalResultSet.getRows()) {
            Map<ColumnRef, Object> values = new HashMap<>();
            for (SelectedItem selectedItem : selectedItems) {
                if (selectedItem instanceof DatabaseTableReference) {
                    String tableName =
                            ((DatabaseTableReference) selectedItem).getTableName();
                    String schemaName =
                            ((DatabaseTableReference) selectedItem).getSchemaName();
                    for (ColumnRef cr : internalResultSet.getColumns()) {
                        if (cr.getSchemaName().equals(schemaName) &&
                                cr.getTableName().equals(tableName)) {
                            values.put(cr, row.getValues().get(cr));
                        }
                    }
                }
                if (selectedItem instanceof ColumnExpression) {
                    values.put(new ColumnRefImpl(((ColumnExpression) selectedItem).getAlias()),
                            PredicateHelper
                                    .evaluateColumnExpr(row,
                                            (ColumnExpression) selectedItem));
                }
            }
            resultRows.add(new InternalResultRow(values));
        }
        return new InternalResultSet(resultColumns, resultRows);
    }


    private static ResultSetImpl createResultSet(InternalResultSet internalResultSet) {

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


    private void createTable(CreateTableStatement stmt)
            throws TableAlreadyExistsException, NoSuchSchemaException {
        PersistentSchema schema = this.getCurrentSchema();
        PersistentTable table = schema.getTableOrNull(stmt.getTableName());
        if (table != null) {
            throw new TableAlreadyExistsException(schema.getName(),
                    stmt.getTableName());
        }
        schema.addTable(
                new PersistentTable(schema.getName(), stmt.getTableName(),
                        stmt.getColumns()));
    }

    private void insert(InsertStatement stmt)
            throws SqlException {
        this.getTable(stmt).insert(stmt.getColumns(), stmt.getValues());
    }

    private void insert(InsertFromSelectStatement stmt)
            throws SqlException {
        ResultSet resultSet = createResultSet(this.getInternalQueryResult(stmt.getSelectExpression()));
        for (ResultRow row : resultSet.getRows()) {
            List<Object> values = new ArrayList<>();
            for (String column : resultSet.getColumns()) {
                values.add(row.getObject(column));
            }
            this.getTable(stmt).insert(stmt.getColumns(), values);
        }
    }


    private void delete(DeleteStatement stmt)
            throws NoSuchSchemaException, NoSuchTableException {
        this.getTable(stmt).delete(stmt.getPredicate());
    }

    private void update(UpdateStatement stmt)
            throws NoSuchSchemaException, NoSuchTableException {
        PersistentTable table = this.getTable(stmt);
        table.update(stmt);
    }

    private PersistentTable getTable(SqlStatement stmt)
            throws NoSuchSchemaException, NoSuchTableException {
        return this.getCurrentSchema().getTable(stmt.getTableName());
    }


    private @NotNull PersistentSchema getSchema(String schemaName)
            throws NoSuchSchemaException {
        for (PersistentSchema schema : schemas) {
            if (schema.getName().equals(schemaName)) {
                return schema;
            }
        }
        throw new NoSuchSchemaException(schemaName);
    }


    private static InternalResultSet getProductOfResults(List<InternalResultSet> resultSets) {

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
            throws NoSuchColumnException, WrongValueTypeException, AmbiguousColumnNameException {
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
            throws NoSuchColumnException, WrongValueTypeException, AmbiguousColumnNameException {

        InternalResultSet internalResultSet = left.joinWith(right);
        return getFilteredResult(internalResultSet, sc);
    }

    private static InternalResultSet leftOuterJoin(InternalResultSet left,
                                                   InternalResultSet right, Predicate sc)
            throws NoSuchColumnException, WrongValueTypeException, AmbiguousColumnNameException {

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

    private static InternalResultSet rightOuterJoin(InternalResultSet left,
                                                    InternalResultSet right, Predicate sc)
            throws NoSuchColumnException, WrongValueTypeException, AmbiguousColumnNameException {

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

    private InternalResultSet getDataFromTableFromSelect(TableFromSelectReference tr) throws SqlException {
        InternalResultSet internalResultSet = getInternalQueryResult(tr.getSelectExpression());
        if (tr.getAlias().isEmpty()) {
            return internalResultSet;
        }
        String alias = tr.getAlias();
        List<ColumnRef> newColumns = new ArrayList<>();
        for (ColumnRef cr : internalResultSet.getColumns()) {
            newColumns.add(new ColumnRefImpl("", alias, cr.getColumnName()));
        }
        List<InternalResultRow> newRows = new ArrayList<>();
        for (InternalResultRow row : internalResultSet.getRows()) {
            Map<ColumnRef, Object> values = new HashMap<>();
            for (Map.Entry<ColumnRef, Object> entry : row.getValues().entrySet()) {
                values.put(new ColumnRefImpl("", alias, entry.getKey().getColumnName()), entry.getValue());
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
            case TABLE_FROM_SELECT:
                return this.getDataFromTableFromSelect((TableFromSelectReference) tableReference);

        }
        throw new IllegalArgumentException("");
    }

    private PersistentTable getTable(DatabaseTableReference tableReference) throws
            NoSuchTableException, NoSuchSchemaException {
        String schemaName = tableReference.getSchemaName();
        if (schemaName.isEmpty()) {
            schemaName = this.getCurrentSchema().getName();
        }
        for (PersistentSchema schema : schemas) {
            if (schema.getName().equals(schemaName)) {
                return schema.getTable(tableReference.getTableName());
            }
        }
        throw new NoSuchSchemaException(tableReference.getSchemaName());
    }


}
