package sqlapi.queries;

import org.jetbrains.annotations.NotNull;

public interface DropTableQuery extends DatabaseQuery {

    @NotNull String getTableName();
}
