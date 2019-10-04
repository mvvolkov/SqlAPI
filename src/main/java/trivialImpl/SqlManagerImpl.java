package trivialImpl;

import api.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SqlManagerImpl implements SqlManager {

    private final Collection<Database> databases = new ArrayList<>();

    @Override
    public Database createDatabase(String dbName) {
        Database database = new DatabaseImpl(dbName);
        databases.add(new DatabaseImpl(dbName));
        return database;
    }

    @Override
    public void openDatabaseWithTables(String dbName, List<SqlTableDescription> tables) {
    }

    @Override
    public void persistDatabase(String dbName) {
    }

    @Override
    public Database getDatabaseOrNull(String dbName) {
        for (Database database : databases) {
            if (database.getName().equals(dbName)) {
                return database;
            }
        }
        return null;
    }

    @Override
    public List<SelectionResultRow> select(JoinTableOperation joinOperation, List<SelectionUnit> selectionUnits, SelectionCondition selectionCondition) {
        return null;
    }
}
