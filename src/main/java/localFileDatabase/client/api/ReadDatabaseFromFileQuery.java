package localFileDatabase.client.api;

import org.jetbrains.annotations.NotNull;
import sqlapi.metadata.TableMetadata;
import sqlapi.queries.SqlQuery;

import java.util.Collection;

public interface ReadDatabaseFromFileQuery extends SqlQuery {

    @NotNull String getDatabaseName();

    @NotNull Collection<TableMetadata> getTables();

    @NotNull String getFileName();
}
