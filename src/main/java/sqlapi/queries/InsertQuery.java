package sqlapi.queries;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface InsertQuery extends SqlTableQuery {

    @NotNull List<String> getColumns();

    @NotNull List<Object> getValues();
}
