package sqlapi.queries;

import org.jetbrains.annotations.NotNull;

public interface SqlTableQuery extends SqlQuery {

    @NotNull String getDatabaseName();

    @NotNull String getTableName();
}
