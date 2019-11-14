package serverLocalFileImpl.persistent;

import api.exceptions.NoSuchTableException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public final class PersistentSchema implements Serializable {

    public static final long serialVersionUID = -2259554024372972206L;

    private final String name;

    private final Collection<PersistentTable> tables = new ArrayList<>();

    public PersistentSchema(String name) {
        this.name = name;
    }

    public void addTable(PersistentTable table) {
        tables.add(table);
    }


    public String getName() {
        return name;
    }


    public PersistentTable getTableOrNull(String tableName) {
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
