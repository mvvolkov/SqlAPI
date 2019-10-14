package api;

import api.exceptions.DatabaseAlreadyExistsException;
import api.exceptions.NoSuchDatabaseException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface SqlManager {

    void createDatabase(String dbName) throws DatabaseAlreadyExistsException;

    void openDatabaseWithTables(String dbName, List<TableDescription> tables);

    void persistDatabase(String dbName);

    @Nullable
    Database getDatabaseOrNull(String dbName);

    @NotNull
    Database getDatabase(String dbName) throws NoSuchDatabaseException;

    List<SqlSelectionResultRow> select(JoinTableOperation joinOperation, List<SelectionUnit> selectionUnits, SqlSelectionCondition selectionCondition);
}
