package sqlapi.server;

import org.jetbrains.annotations.NotNull;
import sqlapi.exceptions.FailedDatabaseValidationException;
import sqlapi.exceptions.SqlException;
import sqlapi.metadata.TableMetadata;
import sqlapi.queries.SelectQuery;
import sqlapi.queries.SqlQuery;
import sqlapi.queryResult.QueryResult;

public interface SqlServer {

    void executeQuery(@NotNull SqlQuery query, Object... parameters) throws SqlException;

    @NotNull QueryResult getQueryResult(@NotNull SelectQuery selectQuery,
                                        Object... parameters)
            throws SqlException;

    void validateMetadata(String databaseName, TableMetadata... tables)
            throws FailedDatabaseValidationException;

    void connect() throws SqlException;

    void close() throws SqlException;
}
