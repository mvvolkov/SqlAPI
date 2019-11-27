package LocalFileDatabase.client.api;

import org.jetbrains.annotations.NotNull;
import sqlapi.queries.SqlQuery;

public interface SaveDatabaseToFileQuery extends SqlQuery {

    @NotNull String getDatabaseName();

    @NotNull String getFileName();
}
