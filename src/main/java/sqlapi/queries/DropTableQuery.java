package sqlapi.queries;

import org.jetbrains.annotations.NotNull;

public interface DropTableQuery extends SqlQuery {

    @NotNull String getDatabaseName();

    @NotNull String getTableName();
}
