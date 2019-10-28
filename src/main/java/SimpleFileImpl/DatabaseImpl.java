package SimpleFileImpl;

import sqlapi.Database;
import sqlapi.Table;
import sqlapi.dbMetadata.TableMetadata;
import sqlapi.exceptions.NoSuchDatabaseException;
import sqlapi.exceptions.NoSuchTableException;
import sqlapi.exceptions.TableAlreadyExistsException;

import java.util.ArrayList;
import java.util.Collection;

public class DatabaseImpl implements Database {


    private final SqlManagerImpl sqlManager;

    private final String name;

    private final Collection<Table> tables = new ArrayList<>();

    public DatabaseImpl(SqlManagerImpl sqlManager, String name) {
        this.sqlManager = sqlManager;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Table getTableOrNull(String tableName) {
        for (Table table : tables) {
            if (table.getMetadata().getName().equals(tableName)) {
                return table;
            }
        }
        return null;
    }

    @Override
    public Table getTable(String tableName) throws NoSuchTableException {
        Table table = this.getTableOrNull(tableName);
        if (table == null) {
            throw new NoSuchTableException(tableName);
        }
        return table;
    }

    @Override
    public void createTable(TableMetadata tableMetadata) throws TableAlreadyExistsException {
        for (Table table : tables) {
            if (table.getMetadata().getName().equals(tableMetadata.getName())) {
                throw new TableAlreadyExistsException(tableMetadata, table.getMetadata());
            }
        }
        tables.add(new TableImpl(this, tableMetadata));
        try {
            this.getLoggerDatabase().createTable(tableMetadata);
        } catch (NoSuchDatabaseException e) {
            e.printStackTrace();
        }
    }

    Database getLoggerDatabase() throws NoSuchDatabaseException {
        return this.getSqlManager().getLogger().getDatabase(name);
    }

    @Override
    public void dropTable(String tableName) {
    }

    public SqlManagerImpl getSqlManager() {
        return sqlManager;
    }
}
