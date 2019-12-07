package sqlapi.queries;

import org.jetbrains.annotations.NotNull;

public interface TableQuery extends DatabaseQuery, ParametrizedQuery {

    @NotNull String getTableName();
}
