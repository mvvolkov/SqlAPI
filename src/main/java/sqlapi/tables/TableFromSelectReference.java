package sqlapi.tables;

import org.jetbrains.annotations.NotNull;
import sqlapi.queries.SelectQuery;

public interface TableFromSelectReference extends TableReference {

    @NotNull String getAlias();

    @NotNull SelectQuery getSelectQuery();

}
