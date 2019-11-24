package sqlapi.queries;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface InsertFromSelectQuery extends SqlTableQuery {

    @NotNull List<String> getColumns();

    @NotNull SelectQuery getSelectQuery();
}
