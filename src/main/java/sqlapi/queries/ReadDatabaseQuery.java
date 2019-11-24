package sqlapi.queries;

import org.jetbrains.annotations.NotNull;
import sqlapi.metadata.TableMetadata;

import java.util.Collection;

public interface ReadDatabaseQuery extends SqlQuery {

    @NotNull String getDatabaseName();

    @NotNull Collection<TableMetadata> getTables();
}
