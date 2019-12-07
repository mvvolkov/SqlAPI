package localFileDatabase.client.api;

import org.jetbrains.annotations.NotNull;
import sqlapi.queries.SqlQuery;

public interface DatabaseFileQuery extends SqlQuery {

    @NotNull String getDatabaseName();

    @NotNull String getFileName();
}
