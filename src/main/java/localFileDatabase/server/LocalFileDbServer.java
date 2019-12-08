package localFileDatabase.server;

import clientImpl.stringUtil.QueryStringUtil;
import localFileDatabase.client.api.DatabaseFileQuery;
import localFileDatabase.client.api.ReadDatabaseFromFileQuery;
import localFileDatabase.client.api.SaveDatabaseToFileQuery;
import localFileDatabase.server.intermediate.InternalQueryResult;
import localFileDatabase.server.persistent.PersistentDatabase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sqlapi.exceptions.*;
import sqlapi.queries.*;
import sqlapi.queryResult.QueryResult;
import sqlapi.server.SqlServer;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;


public final class LocalFileDbServer implements SqlServer {

    @NotNull
    private final Collection<PersistentDatabase> databases = new ArrayList<>();

    LocalFileDbServer() {
        System.out.println("New local file SQL server instantiated.");
    }

    public static LocalFileDbServer getInstance() {
        return new LocalFileDbServer();
    }

    @Override
    public void executeQuery(@NotNull SqlQuery query, Object... parameters)
            throws SqlException {

        if (query instanceof ParametrizedQuery) {
            ((ParametrizedQuery) query).setParameters(parameters);
        }

        try {
            QueryStringUtil.printQuery(query);
        } catch (SqlException se) {
            System.out.println(se.getMessage());
        }

        if (query instanceof DatabaseQuery) {
            DatabaseQuery databaseQuery = (DatabaseQuery) query;
            this.getDatabase(databaseQuery.getDatabaseName()).executeQuery(databaseQuery);
        } else if (query instanceof CreateDatabaseQuery) {
            this.createDatabase((CreateDatabaseQuery) query);
        } else if (query instanceof DatabaseFileQuery) {
            this.executeDatabaseFileQuery((DatabaseFileQuery) query);
        } else {
            throw new UnsupportedQueryTypeException(query);
        }
    }


    @Override
    public @NotNull QueryResult getQueryResult(@NotNull SelectQuery selectQuery,
                                               Object... parameters)
            throws SqlException {
        selectQuery.setParameters(parameters);
        try {
            QueryStringUtil.printSelectQuery(selectQuery);
        } catch (SqlException se) {
            System.out.println(se.getMessage());
        }
        return new InternalQueryResult(this).getQueryResult(selectQuery);
    }


    @Override
    public void connect() {
    }

    @Override
    public void close() {
    }


    private void createDatabase(@NotNull CreateDatabaseQuery query)
            throws DatabaseAlreadyExistsException {
        String databaseName = query.getDatabaseName();
        PersistentDatabase database = this.getDatabaseOrNull(databaseName);
        if (database != null) {
            if (query.checkExistence()) {
                return;
            }
            throw new DatabaseAlreadyExistsException(databaseName);
        }
        databases.add(new PersistentDatabase(databaseName, this));
    }

    private void executeDatabaseFileQuery(@NotNull DatabaseFileQuery query)
            throws SqlException {
        try {
            if (query instanceof ReadDatabaseFromFileQuery) {
                this.readDatabase((ReadDatabaseFromFileQuery) query);
            } else if (query instanceof SaveDatabaseToFileQuery) {
                this.saveDatabase((SaveDatabaseToFileQuery) query);
            } else {
                throw new UnsupportedQueryTypeException(query);
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new WrappedException(e);
        }
    }


    private void readDatabase(@NotNull ReadDatabaseFromFileQuery query)
            throws IOException, ClassNotFoundException {
        PersistentDatabase database = this.getDatabaseOrNull(query.getDatabaseName());
        if (database != null) {
            databases.remove(database);
        }

        ObjectInputStream ois = null;
        try {
            FileInputStream fis = new FileInputStream(query.getFileName());
            ois = new ObjectInputStream(fis);
            database = (PersistentDatabase) ois.readObject();
            database.setServer(this);
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


    @NotNull
    public PersistentDatabase getDatabase(@NotNull String name)
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
            if (database.getDatabaseName().equals(name)) {
                return database;
            }
        }
        return null;
    }
}
