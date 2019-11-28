package sqlapi.queryResult;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface QueryResult {

    @NotNull List<String> getHeaders();

    @NotNull List<QueryResultRow> getRows();
}