package localFileDatabase.server;

import clientImpl.stringUtil.QueryStringUtil;
import localFileDatabase.client.api.ReadDatabaseFromFileQuery;
import localFileDatabase.client.api.SaveDatabaseToFileQuery;
import localFileDatabase.server.intermediate.InternalQueryResult;
import localFileDatabase.server.persistent.PersistentDatabase;
import localFileDatabase.server.persistent.PersistentTable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sqlapi.exceptions.*;
import sqlapi.metadata.TableMetadata;
import sqlapi.queries.*;
import sqlapi.queryResult.QueryResult;
import sqlapi.server.SqlServer;
import sqlapi.tables.DatabaseTableReference;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;


public final class LocalFileDbServer implements SqlServer {

    @NotNull
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
                this.executeInsertFromSelectQuery((InsertFromSelectQuery) query);
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
        return new InternalQueryResult(this).getQueryResult(selectQuery);
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

    @NotNull
    public PersistentTable getTable(@NotNull DatabaseTableReference tableReference) throws
            NoSuchTableException, NoSuchDatabaseException {
        return this.getDatabase(tableReference.getDatabaseName())
                .getTable(tableReference.getTableName());
    }

    private void createDatabase(@NotNull CreateDatabaseQuery query)
            throws DatabaseAlreadyExistsException {
        String databaseName = query.getDatabaseName();
        PersistentDatabase database = this.getDatabaseOrNull(databaseName);
        if (database != null) {
            throw new DatabaseAlreadyExistsException(databaseName);
        }
        databases.add(new PersistentDatabase(databaseName));
    }


    private void readDatabase(@NotNull ReadDatabaseFromFileQuery query)
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


    private void saveDatabase(@NotNull SaveDatabaseToFileQuery query)
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

    private void executeInsertFromSelectQuery(@NotNull InsertFromSelectQuery query)
            throws SqlException {
        QueryResult queryResult = new InternalQueryResult(this).getQueryResult(
                (query.getSelectQuery()));
        this.getDatabase(query.getDatabaseName()).getTable(query.getTableName())
                .insert(query, queryResult);
    }


    private void executeTableQuery(@NotNull TableActionQuery query) throws SqlException {
        this.getDatabase(query.getDatabaseName()).getTable(query.getTableName())
                .executeQuery(query);
    }

    private void createTable(@NotNull CreateTableQuery query)
            throws NoSuchDatabaseException, TableAlreadyExistsException,
            WrongValueTypeException {
        this.getDatabase(query.getDatabaseName())
                .createTable(query.getTableMetadata());
    }

    private void dropTable(@NotNull DropTableQuery query)
            throws NoSuchDatabaseException, NoSuchTableException {
        this.getDatabase(query.getDatabaseName()).dropTable(query.getTableName());
    }

    @NotNull
    private PersistentDatabase getDatabase(@NotNull String name)
            throws NoSuchDatabaseException {
        PersistentDatabase database = this.getDatabaseOrNull(name);
        if (database != null) {
            return database;
        }
        throw new NoSuchDatabaseException(name);
    }

    @Nullable
    private PersistentDatabase getDatabaseOrNull(@Nullable String name) {
        for (PersistentDatabase database : databases) {
            if (database.getName().equals(name)) {
                return database;
            }
        }
        return null;
    }


}
