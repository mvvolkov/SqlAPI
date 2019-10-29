package SimplePrintOutImpl;

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
                throw new TableAlreadyExistsException(tableMetadata.getName());
            }
        }
        tables.add(new TableImpl(this, tableMetadata));
        StringBuilder sb = new StringBuilder("CREATE TABLE ");
        sb.append(tableMetadata.toString());
        System.out.println(sb);
    }


    @Override
    public void dropTable(String tableName) {
    }
}
