package localFileDatabase.server.persistent;

import localFileDatabase.server.LocalFileDbServer;
import sqlapi.exceptions.*;
import sqlapi.metadata.TableMetadata;
import sqlapi.queries.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public final class PersistentDatabase implements Serializable {

    public static final long serialVersionUID = -5620084460569971790L;

    private final String databaseName;

    private final Collection<PersistentTable> tables = new ArrayList<>();


    transient private LocalFileDbServer server;

    public PersistentDatabase(String databaseName, LocalFileDbServer server) {
        this.databaseName = databaseName;
        this.server = server;
    }

    public LocalFileDbServer getServer() {
        return server;
    }

    public void setServer(LocalFileDbServer server) {
        this.server = server;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public Collection<TableMetadata> getTables() {
        return new ArrayList<>(tables);
    }

    public void executeQuery(DatabaseQuery query) throws SqlException {

        if (query instanceof ValidateDatabaseQuery) {
            this.validate(((ValidateDatabaseQuery) query).getTables());
        } else if (query instanceof CreateTableQuery) {
            this.createTable(((CreateTableQuery) query).getTableMetadata());
        } else if (query instanceof DropTableQuery) {
            this.dropTable(((DropTableQuery) query).getTableName());
        } else if (query instanceof TableQuery) {
            this.getTable(((TableQuery) query).getTableName())
                    .executeQuery((TableQuery) query);
        }
    }

    private void validate(Collection<TableMetadata> otherTables)
            throws FailedDatabaseValidationException {

        if (tables.size() != otherTables.size()) {
            throw new FailedDatabaseValidationException("number of tables is different " +
                    "in database " + databaseName);
        }
        for (TableMetadata tableMetadata : otherTables) {
            try {
                this.getTable(tableMetadata.getTableName())
                        .validate(tableMetadata.getColumnsMetadata());
            } catch (NoSuchTableException e) {
                throw new FailedDatabaseValidationException(
                        "table " + tableMetadata.getTableName() + " not found in the " +
                                "database " + databaseName);
            }
        }
    }


    private void createTable(TableMetadata tableMetadata)
            throws SqlException {

        PersistentTable table = this.getTableOrNull(tableMetadata.getTableName());
        if (table != null) {
            throw new TableAlreadyExistsException(databaseName,
                    tableMetadata.getTableName());
        }
        tables.add(new PersistentTable(tableMetadata, this));
    }

    private void dropTable(String tableName) throws NoSuchTableException {
        PersistentTable table = this.getTable(tableName);
        tables.remove(table);
    }


    private PersistentTable getTableOrNull(String tableName) {
        for (PersistentTable table : tables) {
            if (table.getTableName().equals(tableName)) {
                return table;
            }
        }
        return null;
    }

    public PersistentTable getTable(String tableName) throws NoSuchTableException {
        PersistentTable table = this.getTableOrNull(tableName);
        if (table == null) {
            throw new NoSuchTableException(tableName);
        }
        return table;
    }
}
