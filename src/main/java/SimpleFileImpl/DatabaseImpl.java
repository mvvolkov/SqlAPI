package SimpleFileImpl;

import sqlapi.Database;
import sqlapi.Table;
import sqlapi.TableMetadata;
import sqlapi.exceptions.NoSuchTableException;
import sqlapi.exceptions.TableAlreadyExistsException;

import java.util.ArrayList;
import java.util.Collection;

public class DatabaseImpl implements Database {

    private final String name;

    private final Collection<Table> tables = new ArrayList<>();

    public DatabaseImpl(String name) {
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
        return null;
    }

    @Override
    public void createTable(TableMetadata tableMetadata) throws TableAlreadyExistsException {
        tables.add(new TableImpl(this, tableMetadata));
    }

    @Override
    public void dropTable(String tableName) {
    }
}
