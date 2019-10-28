package sqlapi;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sqlapi.dbMetadata.TableMetadata;
import sqlapi.exceptions.*;
import sqlapi.selectionResult.ResultSet;

import java.util.List;

public interface SqlManager {

    void createDatabase(String dbName) throws DatabaseAlreadyExistsException;

    void openDatabaseWithTables(String dbName, List<TableMetadata> tables);

    void persistDatabase(String dbName);

    @Nullable
    Database getDatabaseOrNull(String dbName);

    @NotNull
    Database getDatabase(String dbName) throws NoSuchDatabaseException;

    @NotNull
    ResultSet select(SelectExpression selectExpression) throws WrongValueTypeException, NoSuchTableException, NoSuchDatabaseException, NoSuchColumnException;
}
