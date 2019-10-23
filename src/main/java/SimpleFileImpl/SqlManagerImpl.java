package SimpleFileImpl;

import org.jetbrains.annotations.NotNull;
import sqlapi.*;
import sqlapi.dbMetadata.TableMetadata;
import sqlapi.exceptions.DatabaseAlreadyExistsException;
import sqlapi.exceptions.NoSuchDatabaseException;
import sqlapi.join.JoinTableOperation;
import sqlapi.selectionPredicate.AbstractPredicate;
import sqlapi.selectionResult.SelectionResultRow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SqlManagerImpl implements SqlManager {

    private final Collection<Database> databases = new ArrayList<>();

    @Override
    public void createDatabase(String dbName) throws DatabaseAlreadyExistsException {
        for (Database database : databases) {
            if (database.getName().equals(dbName)) {
                throw new DatabaseAlreadyExistsException(dbName);
            }
        }
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
        for (Database database : databases) {
            if (database.getName().equals(dbName)) {
                return database;
            }
        }
        return null;
    }

    @Override
    public @NotNull Database getDatabase(String dbName) throws NoSuchDatabaseException {
        Database database = this.getDatabaseOrNull(dbName);
        if (database == null) {
            throw new NoSuchDatabaseException(dbName);
        }
        return database;
    }

    @Override
    public List<SelectionResultRow> select(JoinTableOperation joinOperation, List<SelectionUnit> selectionUnits, AbstractPredicate selectionPredicate) {
        return null;
    }


}
