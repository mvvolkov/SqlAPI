package sqlapi.server;

import org.jetbrains.annotations.NotNull;
import sqlapi.exceptions.SqlException;
import sqlapi.metadata.TableMetadata;
import sqlapi.queries.SelectQuery;
import sqlapi.queries.SqlQuery;
import sqlapi.queryResult.ResultSet;

import java.util.Collection;

public interface SqlServer {


    void executeQuery(@NotNull SqlQuery query) throws SqlException;

    @NotNull ResultSet getQueryResult(@NotNull SelectQuery selectQuery)
            throws SqlException;

    @NotNull Collection<String> getDatabases() throws SqlException;

    @NotNull Collection<TableMetadata> getTables(@NotNull String databaseName)
            throws SqlException;
}
