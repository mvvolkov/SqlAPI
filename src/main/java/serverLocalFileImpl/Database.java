package serverLocalFileImpl;

import api.exceptions.NoSuchTableException;

import java.util.ArrayList;
import java.util.Collection;

public class Database {


    private final String name;

    private final Collection<Table> tables = new ArrayList<>();

    public Database(String name) {
        this.name = name;
    }

    public void addTable(Table table) {
        tables.add(table);
    }


    public String getName() {
        return name;
    }


    public Table getTableOrNull(String tableName) {
        for (Table table : tables) {
            if (table.getTableName().equals(tableName)) {
                return table;
            }
        }
        return null;
    }


    public Table getTable(String tableName) throws NoSuchTableException {
        Table table = this.getTableOrNull(tableName);
        if (table == null) {
            throw new NoSuchTableException(tableName);
        }
        return table;
    }
}
