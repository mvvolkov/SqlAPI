package SimplePrintOutImpl;

import api.Database;
import api.Table;
import api.TableDescription;
import api.exceptions.NoSuchTableException;
import api.exceptions.TableAlreadyExistsException;

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
            if (table.getDescription().getName().equals(tableName)) {
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
    public void createTable(TableDescription tableDescription) throws TableAlreadyExistsException {
        for (Table table : tables) {
            if (table.getDescription().getName().equals(tableDescription.getName())) {
                throw new TableAlreadyExistsException(tableDescription, table.getDescription());
            }
        }
        tables.add(new SqlTableImpl(this, tableDescription));
        StringBuilder sb = new StringBuilder("CREATE TABLE ");
        sb.append(tableDescription.toString());
        System.out.println(sb);
    }


    @Override
    public void dropTable(String tableName) {
    }
}
