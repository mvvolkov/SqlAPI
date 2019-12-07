package sqlapi.queries;

import org.jetbrains.annotations.NotNull;
import sqlapi.metadata.TableMetadata;

public interface CreateTableQuery extends DatabaseQuery {

    @NotNull TableMetadata getTableMetadata();
}
