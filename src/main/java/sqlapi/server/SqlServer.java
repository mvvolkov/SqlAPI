package sqlapi.server;

import org.jetbrains.annotations.NotNull;
import sqlapi.exceptions.SqlException;
import sqlapi.metadata.TableMetadata;
import sqlapi.queries.SelectQuery;
import sqlapi.queries.SqlQuery;
import sqlapi.queryResult.QueryResult;

import java.util.Collection;

public interface SqlServer {


    void executeQuery(@NotNull SqlQuery query, Object... parameters) throws SqlException;

    @NotNull QueryResult getQueryResult(@NotNull SelectQuery selectQuery,
                                        Object... parameters)
            throws SqlException;


    @NotNull Collection<String> getDatabases() throws SqlException;

    @NotNull Collection<TableMetadata> getTables(@NotNull String databaseName)
            throws SqlException;

    void connect() throws SqlException;

    void close() throws SqlException;
}
