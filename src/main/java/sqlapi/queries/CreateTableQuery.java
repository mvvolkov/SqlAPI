package sqlapi.queries;

import org.jetbrains.annotations.NotNull;
import sqlapi.metadata.TableMetadata;

public interface CreateTableQuery extends SqlQuery {

    @NotNull String getDatabaseName();

    @NotNull TableMetadata getTableMetadata();
}
