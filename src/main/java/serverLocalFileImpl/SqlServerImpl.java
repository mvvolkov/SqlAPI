package serverLocalFileImpl;

import api.SelectedItem;
import api.SqlServer;
import api.columnExpr.ColumnExpression;
import api.columnExpr.ColumnRef;
import api.exceptions.*;
import api.metadata.TableMetadata;
import api.predicates.Predicate;
import api.queries.*;
import api.selectResult.ResultRow;
import api.selectResult.ResultSet;
import api.tables.DatabaseTableReference;
import api.tables.JoinTableReference;
import api.tables.TableReference;
import org.jetbrains.annotations.NotNull;
import serverLocalFileImpl.persistent.PersistentSchema;
import serverLocalFileImpl.persistent.PersistentTable;
import sqlFactory.SqlManagerFactory;

import java.util.*;

public final class SqlServerImpl implements SqlServer {


    private final transient SqlServer logger =
            SqlManagerFactory.getServerLoggerSqlManager();

    private final Collection<PersistentSchema> schemas = new ArrayList<>();

    private final PersistentSchema defaultSchema;

    public SqlServerImpl() {
        defaultSchema = new PersistentSchema("DB1");
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
        return this.select(se);
    }

    private ResultSet select(SelectExpression se) throws
            SqlException {

        List<InternalResultSet> resultSets = new ArrayList<>();
        for (TableReference tr : se.getTableReferences()) {
            resultSets.add(this.getDataFromTableRef(tr));
        }
        InternalResultSet internalResultSet = getJoinedResult(resultSets);
        internalResultSet = getFilteredResult(internalResultSet, se.getPredicate());
        return createResultSet(internalResultSet, se.getSelectedItems());
    }


    private static ResultSetImpl createResultSet(InternalResultSet internalResultSet,
                                                 List<SelectedItem> selectedItems)
            throws NoSuchColumnException {

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
                        if (cr.getSchemaName().equals(dbName) &&
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
                            if (cr.getSchemaName().equals(dbName) &&
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
        ResultSet resultSet = this.select(stmt.getSelectExpression());
        for (ResultRow row : resultSet.getRows()) {
            this.getTable(stmt).insert(stmt.getColumns(), row.getValues());
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


    private static InternalResultSet getJoinedResult(List<InternalResultSet> resultSets) {

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

    private static InternalResultSet leftOuterJoin(InternalResultSet left,
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

    private static InternalResultSet rightOuterJoin(InternalResultSet left,
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


    private InternalResultSet getDataFromDatabaseTable(DatabaseTableReference dtr)
            throws SqlException {
        return this.getTable(dtr).getData();
    }

    private InternalResultSet getDataFromJoinedTable(JoinTableReference tableReference)
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
                return this.getDataFromJoinedTable((JoinTableReference) tableReference);
            case SELECT_EXPRESSION:

        }
        throw new IllegalArgumentException("");
    }

    private PersistentTable getTable(DatabaseTableReference tableReference) throws
            NoSuchTableException, NoSuchSchemaException {
        for (PersistentSchema database : schemas) {
            if (database.getName().equals(tableReference.getDatabaseName())) {
                return (PersistentTable) database.getTable(tableReference.getTableName());
            }
        }
        throw new NoSuchSchemaException(tableReference.getDatabaseName());
    }


}
