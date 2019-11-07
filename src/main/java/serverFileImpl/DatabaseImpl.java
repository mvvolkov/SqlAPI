package serverFileImpl;

import api.Database;
import api.Table;
import api.TableMetadata;
import api.exceptions.NoSuchDatabaseException;
import api.exceptions.NoSuchTableException;
import api.exceptions.TableAlreadyExistsException;

import java.util.ArrayList;
import java.util.Collection;

public class DatabaseImpl implements Database {


    private final SqlServerImpl sqlManager;

    private final String name;

    private final Collection<Table> tables = new ArrayList<>();

    public DatabaseImpl(SqlServerImpl sqlManager, String name) {
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
                throw new TableAlreadyExistsException(name, tableMetadata.getName());
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

    public SqlServerImpl getSqlManager() {
        return sqlManager;
    }
}
