package sqlapi.tables;

import org.jetbrains.annotations.NotNull;
import sqlapi.queries.SelectQuery;

import java.util.List;

public interface TableFromSelectReference extends TableReference {

    @NotNull String getAlias();

    @NotNull SelectQuery getSelectQuery();

    @Override default void setParameters(List<Object> parameters) {
        getSelectQuery().setParameters(parameters);
    }
}
