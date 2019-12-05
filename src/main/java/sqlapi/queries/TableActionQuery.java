package sqlapi.queries;

import org.jetbrains.annotations.NotNull;

public interface TableActionQuery extends SqlQuery {

    @NotNull String getDatabaseName();

    @NotNull String getTableName();

    void setParameters(Object... values);
}
