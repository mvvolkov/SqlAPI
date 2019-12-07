package sqlapi.queries;

import org.jetbrains.annotations.NotNull;

public interface DatabaseQuery extends SqlQuery {

    @NotNull String getDatabaseName();
}
