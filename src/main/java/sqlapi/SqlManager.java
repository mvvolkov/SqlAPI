package sqlapi;

import sqlapi.dbMetadata.TableMetadata;
import sqlapi.exceptions.DatabaseAlreadyExistsException;
import sqlapi.exceptions.NoSuchDatabaseException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sqlapi.join.JoinTableOperation;
import sqlapi.selectionPredicate.AbstractPredicate;
import sqlapi.selectionResult.SelectionResultRow;

import java.util.List;

public interface SqlManager {

    void createDatabase(String dbName) throws DatabaseAlreadyExistsException;

    void openDatabaseWithTables(String dbName, List<TableMetadata> tables);

    void persistDatabase(String dbName);

    @Nullable
    Database getDatabaseOrNull(String dbName);

    @NotNull
    Database getDatabase(String dbName) throws NoSuchDatabaseException;

    List<SelectionResultRow> select(JoinTableOperation joinOperation, List<SelectionUnit> selectionUnits, AbstractPredicate selectionPredicate);
}
