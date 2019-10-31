package SimplePrintOutImpl;

import org.jetbrains.annotations.NotNull;
import sqlapi.Database;
import sqlapi.SelectExpression;
import sqlapi.TableMetadata;
import sqlapi.SqlManager;
import sqlapi.exceptions.DatabaseAlreadyExistsException;
import sqlapi.exceptions.NoSuchDatabaseException;
import sqlapi.exceptions.NoSuchTableException;
import sqlapi.exceptions.WrongValueTypeException;
import sqlapi.selectionResult.ResultSet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SqlManagerPrintOutImpl implements SqlManager {

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
    public Database getDatabase(String dbName) throws NoSuchDatabaseException {
        Database database = this.getDatabaseOrNull(dbName);
        if (database == null) {
            throw new NoSuchDatabaseException(dbName);
        }
        return database;
    }

    @Override
    public @NotNull ResultSet select(SelectExpression selectExpression) throws WrongValueTypeException, NoSuchTableException {
        return null;
    }


}
