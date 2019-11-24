package sqlapi.queries;

import org.jetbrains.annotations.NotNull;

public interface CreateDatabaseQuery extends SqlQuery {

    @NotNull String getDatabaseName();
}
