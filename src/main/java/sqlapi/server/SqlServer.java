package sqlapi.server;

import org.jetbrains.annotations.NotNull;
import sqlapi.exceptions.*;
import sqlapi.metadata.TableMetadata;
import sqlapi.queries.SelectQuery;
import sqlapi.queries.SqlQuery;
import sqlapi.selectResult.ResultSet;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;

public interface SqlServer {

    void createDatabase(String databaseName) throws DatabaseAlreadyExistsException;

    void readDatabase(String fileName, String databaseName,
                      Collection<TableMetadata> tables)
            throws IOException, ClassNotFoundException, NoSuchTableException,
            NoSuchColumnException;

    void saveDatabase(String databaseName, String fileName)
            throws IOException, NoSuchDatabaseException;


    void executeQuery(@NotNull SqlQuery query) throws SqlException;

    @NotNull ResultSet getQueryResult(@NotNull SelectQuery selectQuery)
            throws SqlException;
}
