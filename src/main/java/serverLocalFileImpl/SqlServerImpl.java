package serverLocalFileImpl;

import serverLocalFileImpl.intermediateresult.DataGroup;
import serverLocalFileImpl.intermediateresult.DataHeader;
import serverLocalFileImpl.intermediateresult.DataRow;
import serverLocalFileImpl.intermediateresult.DataSet;
import sqlapi.columnExpr.ColumnExpression;
import sqlapi.columnExpr.ColumnRef;
import sqlapi.server.SqlServer;
import sqlapi.exceptions.*;
import sqlapi.metadata.TableMetadata;
import sqlapi.misc.SelectedItem;
import sqlapi.predicates.Predicate;
import sqlapi.queries.SelectExpression;
import sqlapi.queries.SqlStatement;
import sqlapi.selectResult.ResultRow;
import sqlapi.selectResult.ResultSet;
import sqlapi.tables.DatabaseTableReference;
import sqlapi.tables.JoinedTableReference;
import sqlapi.tables.TableFromSelectReference;
import sqlapi.tables.TableReference;
import org.jetbrains.annotations.NotNull;
import serverLocalFileImpl.persistent.PersistentColumnMetadata;
import serverLocalFileImpl.persistent.PersistentDatabase;
import serverLocalFileImpl.persistent.PersistentTable;
import ServerFactory.SqlManagerFactory;

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

    @Override public void createSchema(String dbName, String schemaName)
            throws NoSuchDatabaseException, SchemaAlreadyExistsException {
        logger.createSchema(dbName, schemaName);
        this.getDatabase(dbName).createSchema(schemaName);
    }

    @Override public void setCurrentSchema(String dbName, String schemaName)
            throws NoSuchDatabaseException, NoSuchSchemaException {
        logger.setCurrentSchema(dbName, schemaName);
        this.getDatabase(dbName).setCurrentSchema(schemaName);
    }

    @Override
    public void openDatabaseWithTables(String dbName, List<TableMetadata> tables) {
    }


    @Override
    public void persistDatabase(String dbName) {
    }

    @Override
    public void executeStatement(SqlStatement statement) throws SqlException {
        logger.executeStatement(statement);
        this.getDatabase(statement.getDatabaseName()).executeStatement(statement);
    }

    @Override
    public ResultSet getQueryResult(SelectExpression selectExpression)
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
    public DataSet getInternalQueryResult(SelectExpression se) throws
            SqlException {

        DataSet result = this.getProductOfTables(se.getTableReferences());
        result = getFilteredResult(result, se.getPredicate());

        if (se.getSelectedItems().isEmpty()) {
            return result;
        }

        List<ColumnExpression> columnExpressions =
                this.getSelectedColumnExpressions(se.getSelectedItems());

        List<DataHeader> headers = getSelectedColumns(columnExpressions);
        List<DataRow> resultRows;
        if (!se.getGroupByColumns().isEmpty()) {
            Collection<DataGroup> groups =
                    getGroups(se.getGroupByColumns(), result.getRows());

            resultRows = new ArrayList<>();
            for (DataGroup group : groups) {
                Map<DataHeader, Object> values = new HashMap<>();
                for (ColumnExpression ce : columnExpressions) {
                    String columnName = ce.getAlias().isEmpty() ? ce.toString() :
                            ce.getAlias();
                    values.put(new DataHeader(columnName),
                            group.evaluateColumnExpr(ce));
                }
                resultRows.add(new DataRow(values));
            }
        } else {
            resultRows = getSelectedValues(result, headers, columnExpressions);
        }
        return new DataSet(headers, resultRows);
    }

    private static Collection<DataGroup> getGroups(
            List<ColumnRef> groupByColumns,
            Collection<DataRow> rows)
            throws NoSuchColumnException, AmbiguousColumnNameException {
        Map<List<Object>, List<DataRow>> map = new HashMap<>();
        for (DataRow row : rows) {
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
        Collection<DataGroup> groups = new ArrayList<>();
        for (Map.Entry<List<Object>, List<DataRow>> entry : map.entrySet()) {
            groups.add(new DataGroup(groupByColumns, entry.getKey(),
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
                    columnExpressions.add(new ColumnRef() {

                        @Override public String getColumnName() {
                            return column.getColumnName();
                        }

                        @Override public String getTableName() {
                            return tableName;
                        }

                        @Override public String getSchemaName() {
                            return schemaName;
                        }
                    });
                }
            }
            if (selectedItem instanceof ColumnExpression) {
                columnExpressions.add((ColumnExpression) selectedItem);
            }
        }
        return columnExpressions;

    }

    private static List<DataHeader> getSelectedColumns(
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

    private static List<DataRow> getSelectedValues(
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

    private DataSet getProductOfTables(List<TableReference> tableReferences)
            throws SqlException {

        List<DataSet> dataSets = new ArrayList<>();
        for (TableReference tr : tableReferences) {
            dataSets.add(this.getDataFromTableRef(tr));
        }
        if (dataSets.isEmpty()) {
            return new DataSet(Collections.emptyList(),
                    Collections.emptyList());
        }
        Iterator<DataSet> it = dataSets.iterator();
        DataSet resultSet = it.next();
        while (it.hasNext()) {
            resultSet = resultSet.joinWith(it.next());
        }
        return resultSet;
    }

    private static DataSet getFilteredResult(DataSet resultSet,
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

    private static DataSet innerJoin(DataSet left,
                                     DataSet right, Predicate sc)
            throws NoSuchColumnException, WrongValueTypeException,
            AmbiguousColumnNameException, InvalidQueryException {

        DataSet dataSet = left.joinWith(right);
        return getFilteredResult(dataSet, sc);
    }

    private static DataSet leftOuterJoin(DataSet left,
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

    private static DataSet rightOuterJoin(DataSet left,
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


    private DataSet getDataFromPersistentTable(DatabaseTableReference dtr)
            throws SqlException {
        return this.getTable(dtr).getData();
    }

    private DataSet getDataFromJoinedTable(JoinedTableReference tableReference)
            throws SqlException {


        DataSet left =
                this.getDataFromTableRef(tableReference.getLeftTableReference());
        DataSet right =
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

    private DataSet getSubqueryResult(TableFromSelectReference tr)
            throws SqlException {
        DataSet dataSet =
                getInternalQueryResult(tr.getSelectExpression());
        String alias = tr.getAlias();
        if (alias.isEmpty()) {
            return dataSet;
        }
        List<DataHeader> newColumns = new ArrayList<>();
        for (DataHeader cr : dataSet.getColumns()) {
            newColumns.add(new DataHeader(cr.getSqlType(), "", alias,
                    cr.getColumnName()));
        }
        List<DataRow> newRows = new ArrayList<>();
        for (DataRow row : dataSet.getRows()) {
            Map<DataHeader, Object> values = new HashMap<>();
            for (Map.Entry<DataHeader, Object> entry : row.getCells()
                    .entrySet()) {
                values.put(new DataHeader(entry.getKey().getSqlType(), "", alias,
                                entry.getKey().getColumnName()),
                        entry.getValue());
            }
            newRows.add(new DataRow(values));
        }
        return new DataSet(newColumns, newRows);
    }


    private @NotNull DataSet getDataFromTableRef(
            TableReference tableReference) throws
            SqlException {

        switch (tableReference.getTableRefType()) {
            case DATABASE_TABLE:
                return this.getDataFromPersistentTable(
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
