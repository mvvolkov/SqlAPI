package serverLocalFileImpl;

import sqlapi.exceptions.DatabaseAlreadyExistsException;
import sqlapi.exceptions.NoSuchColumnException;
import sqlapi.exceptions.NoSuchDatabaseException;
import sqlapi.exceptions.NoSuchTableException;
import sqlapi.metadata.TableMetadata;
import sqlapi.server.SqlServer;

import java.io.IOException;
import java.util.Collection;

public interface SqlServerLocalFile extends SqlServer {

    void createDatabase(String databaseName) throws DatabaseAlreadyExistsException;

    void readDatabase(String fileName, String databaseName,
                      Collection<TableMetadata> tables)
            throws IOException, ClassNotFoundException, NoSuchTableException,
            NoSuchColumnException;

    void saveDatabase(String databaseName, String fileName)
            throws IOException, NoSuchDatabaseException;
}
