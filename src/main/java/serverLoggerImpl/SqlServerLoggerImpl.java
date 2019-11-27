package serverLoggerImpl;

import org.jetbrains.annotations.NotNull;
import sqlapi.metadata.TableMetadata;
import sqlapi.queries.SelectQuery;
import sqlapi.queries.SqlQuery;
import sqlapi.queryResult.ResultRow;
import sqlapi.queryResult.ResultSet;
import sqlapi.server.SqlServer;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SqlServerLoggerImpl implements SqlServer {


    @Override
    public void executeQuery(@NotNull SqlQuery query) {
        System.out.println(query);
    }

    @Override
    public @NotNull ResultSet getQueryResult(@NotNull SelectQuery selectQuery) {
        System.out.println(selectQuery);
        return new ResultSet() {
            @NotNull
            @Override
            public List<String> getHeaders() {
                return Collections.emptyList();
            }

            @NotNull
            @Override
            public List<ResultRow> getRows() {
                return Collections.emptyList();
            }
        };
    }

    @Override public @NotNull Collection<String> getDatabases() {
        return Collections.emptyList();
    }

    @Override public @NotNull Collection<TableMetadata> getTables(@NotNull String databaseName) {
        return Collections.emptyList();
    }
}
