package SimpleFileImpl;

import sqlapi.*;
import sqlapi.exceptions.NoSuchDatabaseException;
import sqlapi.SelectionCriteria;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SqlManagerImpl implements SqlManager {

    private final Collection<Database> databases = new ArrayList<>();

    @Override
    public void createDatabase(String dbName) {
        Database database = new DatabaseImpl(dbName);
        databases.add(new DatabaseImpl(dbName));
    }

    @Override
    public void openDatabaseWithTables(String dbName, List<TableMetadata> tables) {

    }


    @Override
    public void persistDatabase(String dbName) {

    }

    @Override
    public Database getDatabaseOrNull(String dbName) {
        return null;
    }

    @Override
    public @NotNull Database getDatabase(String dbName) throws NoSuchDatabaseException {
        return null;
    }

    @Override
    public List<SelectionResultRow> select(JoinTableOperation joinOperation, List<SelectionUnit> selectionUnits, SelectionCriteria selectionCriteria) {
        return null;
    }


}
