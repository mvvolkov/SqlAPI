package localFileDatabase.server;

import localFileDatabase.client.api.ReadDatabaseFromFileQuery;
import localFileDatabase.client.api.SaveDatabaseToFileQuery;
import clientImpl.stringUtil.QueryStringUtil;
import org.jetbrains.annotations.NotNull;
import localFileDatabase.server.intermediateResult.*;
import localFileDatabase.server.persistent.PersistentColumnMetadata;
import localFileDatabase.server.persistent.PersistentDatabase;
import localFileDatabase.server.persistent.PersistentTable;
import sqlapi.columnExpr.ColumnExpression;
import sqlapi.columnExpr.ColumnRef;
import sqlapi.exceptions.*;
import sqlapi.metadata.TableMetadata;
import sqlapi.misc.SelectedItem;
import sqlapi.queries.*;
import sqlapi.queryResult.QueryResult;
import sqlapi.server.SqlServer;
import sqlapi.tables.*;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;


public final class LocalFileDbServer implements SqlServer {

    private final Collection<PersistentDatabase> databases = new ArrayList<>();

    LocalFileDbServer() {
        System.out.println("New local file SQL server instantiated.");
    }

    @Override
    public void executeQuery(@NotNull SqlQuery query) throws SqlException {

        try {
            QueryStringUtil.printQuery(query);
        } catch (SqlException se) {
            System.out.println(se.getMessage());
        }
        if (query instanceof CreateDatabaseQuery) {
            this.createDatabase((CreateDatabaseQuery) query);
            return;
        } else if (query instanceof ReadDatabaseFromFileQuery) {
            try {
                this.readDatabase((ReadDatabaseFromFileQuery) query);
            } catch (IOException | ClassNotFoundException e) {
                throw new WrappedException(e);
            }
            return;
        } else if (query instanceof SaveDatabaseToFileQuery) {
            try {
                this.saveDatabase((SaveDatabaseToFileQuery) query);
            } catch (IOException e) {
                throw new WrappedException(e);
            }
            return;
        } else if (query instanceof CreateTableQuery) {
            this.createTable((CreateTableQuery) query);
            return;
        } else if (query instanceof DropTableQuery) {
            this.dropTable((DropTableQuery) query);
            return;
        } else if (query instanceof TableActionQuery) {
            if (query instanceof InsertFromSelectQuery) {
                InsertFromSelectQuery insertFromSelectQuery =
                        (InsertFromSelectQuery) query;
                QueryResult queryResult = DataUtil.createResultSet(
                        this.getInternalQueryResult(
                                (insertFromSelectQuery.getSelectQuery())));
                this.getDatabase(insertFromSelectQuery.getDatabaseName())
                        .insert(insertFromSelectQuery,
                                queryResult);
                return;
            }
            this.executeTableQuery((TableActionQuery) query);
            return;
        }
        throw new UnsupportedQueryTypeException(query);
    }

    @Override
    public @NotNull QueryResult getQueryResult(@NotNull SelectQuery selectQuery)
            throws SqlException {
        try {
            QueryStringUtil.printSelectQuery(selectQuery);
        } catch (SqlException se) {
            System.out.println(se.getMessage());
        }
        return DataUtil.createResultSet(this.getInternalQueryResult(selectQuery));
    }

    @Override
    public @NotNull Collection<String> getDatabases() {
        return databases.stream().map(PersistentDatabase::getName)
                .collect(Collectors.toList());
    }

    @Override
    public @NotNull Collection<TableMetadata> getTables(@NotNull String databaseName)
            throws NoSuchDatabaseException {
        return this.getDatabase(databaseName).getTables();
    }

    @Override
    public void connect() throws SqlException {
    }

    @Override
    public void close() throws SqlException {
    }

    private void createDatabase(CreateDatabaseQuery query)
            throws DatabaseAlreadyExistsException {
        String databaseName = query.getDatabaseName();
        PersistentDatabase database = this.getDatabaseOrNull(databaseName);
        if (database != null) {
            throw new DatabaseAlreadyExistsException(databaseName);
        }
        databases.add(new PersistentDatabase(databaseName));
    }


    private void readDatabase(ReadDatabaseFromFileQuery query)
            throws IOException, ClassNotFoundException, NoSuchTableException,
            NoSuchColumnException {
        PersistentDatabase database = this.getDatabaseOrNull(query.getDatabaseName());
        if (database != null) {
            databases.remove(database);
        }

        ObjectInputStream ois = null;
        try {
            FileInputStream fis = new FileInputStream(query.getFileName());
            ois = new ObjectInputStream(fis);
            database = (PersistentDatabase) ois.readObject();
            database.validate(query.getTables());
            databases.add(database);
        } finally {
            if (ois != null) {
                ois.close();
            }
        }
    }


    private void saveDatabase(SaveDatabaseToFileQuery query)
            throws IOException, NoSuchDatabaseException {
        ObjectOutputStream oos = null;
        try {
            File outputFile = new File(query.getFileName());
            FileOutputStream fos = new FileOutputStream(outputFile);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(this.getDatabase(query.getDatabaseName()));
        } finally {
            if (oos != null) {
                oos.close();
            }
        }
    }


    private void executeTableQuery(TableActionQuery query) throws SqlException {
        this.getDatabase(query.getDatabaseName()).executeQuery(query);
    }

    private void createTable(CreateTableQuery query)
            throws NoSuchDatabaseException, TableAlreadyExistsException,
            WrongValueTypeException {
        this.getDatabase(query.getDatabaseName())
                .createTable(query.getTableMetadata());
    }

    private void dropTable(DropTableQuery query)
            throws NoSuchDatabaseException, NoSuchTableException {
        this.getDatabase(query.getDatabaseName()).dropTable(query.getTableName());
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
    private DataSet getInternalQueryResult(SelectQuery se) throws
            SqlException {

        DataSet result = this.getProductOfTables(se.getTableReferences());
        result = DataUtil.getFilteredResult(result, se.getPredicate());

        if (se.getSelectedItems().isEmpty()) {
            return result;
        }

        List<ColumnExpression> selectedExpressions =
                this.getSelectedColumnExpressions(se.getSelectedItems());

        List<DataHeader> headers = DataUtil.getSelectedColumns(selectedExpressions);
        List<DataRow> resultRows;
        if (!se.getGroupByColumns().isEmpty()) {
            Collection<DataGroup> groups =
                    DataGroup.getGroups(se.getGroupByColumns(), result.getRows());

            resultRows = new ArrayList<>();
            for (DataGroup group : groups) {
                List<DataValue> values = new ArrayList<>();
                for (ColumnExpression ce : selectedExpressions) {
                    String columnName = ce.getAlias().isEmpty() ? ce.toString() :
                            ce.getAlias();

                    values.add(new DataValue(new DataHeader(columnName),
                            group.evaluateColumnExpr(ce).getValue()));
                }
                resultRows.add(new DataRow(values));
            }
        } else {
            resultRows = DataUtil.getSelectedValues(result, headers, selectedExpressions);
        }
        return new DataSet(headers, resultRows);
    }


    private List<ColumnExpression> getSelectedColumnExpressions(
            List<SelectedItem> selectedItems)
            throws NoSuchTableException, NoSuchDatabaseException {

        List<ColumnExpression> columnExpressions = new ArrayList<>();
        for (SelectedItem selectedItem : selectedItems) {
            if (selectedItem instanceof DatabaseTableReference) {
                String databaseName =
                        ((DatabaseTableReference) selectedItem).getDatabaseName();
                String tableName =
                        ((DatabaseTableReference) selectedItem).getTableName();
                List<PersistentColumnMetadata> columns =
                        this.getTable((DatabaseTableReference) selectedItem).getColumns();
                for (PersistentColumnMetadata column : columns) {
                    columnExpressions.add(new ColumnRef() {

                        @Override
                        public @NotNull ColumnExpression add(
                                @NotNull ColumnExpression otherExpression) {
                            throw new UnsupportedOperationException();
                        }

                        @Override
                        public @NotNull ColumnExpression add(
                                @NotNull ColumnExpression otherExpression,
                                @NotNull String alias) {
                            throw new UnsupportedOperationException();
                        }

                        @Override
                        public @NotNull ColumnExpression subtract(
                                @NotNull ColumnExpression otherExpression) {
                            throw new UnsupportedOperationException();
                        }

                        @Override
                        public @NotNull ColumnExpression subtract(
                                @NotNull ColumnExpression otherExpression,
                                @NotNull String alias) {
                            throw new UnsupportedOperationException();
                        }

                        @Override
                        public @NotNull ColumnExpression multiply(
                                @NotNull ColumnExpression otherExpression) {
                            throw new UnsupportedOperationException();
                        }

                        @Override
                        public @NotNull ColumnExpression multiply(
                                @NotNull ColumnExpression otherExpression,
                                @NotNull String alias) {
                            throw new UnsupportedOperationException();
                        }

                        @Override
                        public @NotNull ColumnExpression divide(
                                @NotNull ColumnExpression otherExpression) {
                            throw new UnsupportedOperationException();
                        }

                        @Override
                        public @NotNull ColumnExpression divide(
                                @NotNull ColumnExpression otherExpression,
                                @NotNull String alias) {
                            throw new UnsupportedOperationException();
                        }

                        @NotNull
                        @Override
                        public String getColumnName() {
                            return column.getColumnName();
                        }

                        @NotNull
                        @Override
                        public String getTableName() {
                            return tableName;
                        }

                        @Override
                        public @NotNull String getDatabaseName() {
                            return databaseName;
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
            resultSet = resultSet.productWith(it.next());
        }
        return resultSet;
    }


    private DataSet getDataFromPersistentTable(DatabaseTableReference dtr)
            throws SqlException {
        return this.getTable(dtr).createDataSet();
    }

    private DataSet getDataFromJoinedTable(JoinedTableReference tableReference)
            throws SqlException {

        DataSet left =
                this.getDataFromTableRef(tableReference.getLeftTableReference());
        DataSet right =
                this.getDataFromTableRef(tableReference.getRightTableReference());
        if (tableReference instanceof InnerJoinTableReference) {
            return DataUtil.innerJoin(left, right, tableReference.getPredicate());
        }
        if (tableReference instanceof LeftOuterJoinTableReference) {
            return DataUtil.leftOuterJoin(left, right, tableReference.getPredicate());
        }
        if (tableReference instanceof RightOuterJoinTableReference) {
            return DataUtil
                    .rightOuterJoin(left, right, tableReference.getPredicate());
        }
        throw new UnsupportedTableReferenceTypeException(tableReference);
    }

    private DataSet getSubqueryResult(TableFromSelectReference tr)
            throws SqlException {

        // first, get a result of subquery.
        DataSet dataSet = getInternalQueryResult(tr.getSelectQuery());
        String alias = tr.getAlias();
        if (alias.isEmpty()) {
            return dataSet;
        }

        // second, replace table name with the alias everywhere in the result data.
        List<DataHeader> newColumns = new ArrayList<>();
        for (DataHeader cr : dataSet.getColumns()) {
            newColumns.add(new DataHeader("", alias,
                    cr.getColumnName()));
        }
        List<DataRow> newRows = new ArrayList<>();
        for (DataRow row : dataSet.getRows()) {
            List<DataValue> values = new ArrayList<>();
            for (DataValue value : row.getValues()) {
                DataHeader oldHeader = value.getHeader();
                DataHeader header = new DataHeader("", alias,
                        oldHeader.getColumnName());
                values.add(new DataValue(header, value.getValue()));
            }
            newRows.add(new DataRow(values));
        }
        return new DataSet(newColumns, newRows);
    }


    private @NotNull DataSet getDataFromTableRef(
            TableReference tableReference) throws
            SqlException {

        if (tableReference instanceof DatabaseTableReference) {
            return this.getDataFromPersistentTable(
                    (DatabaseTableReference) tableReference);
        }
        if (tableReference instanceof JoinedTableReference) {
            return this.getDataFromJoinedTable((JoinedTableReference) tableReference);
        }
        if (tableReference instanceof TableFromSelectReference) {
            return this.getSubqueryResult(
                    (TableFromSelectReference) tableReference);
        }
        throw new UnsupportedTableReferenceTypeException(tableReference);
    }

    @NotNull
    private PersistentTable getTable(DatabaseTableReference tableReference) throws
            NoSuchTableException, NoSuchDatabaseException {
        return this.getDatabase(tableReference.getDatabaseName())
                .getTable(tableReference.getTableName());
    }


}
