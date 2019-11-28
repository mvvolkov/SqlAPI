package localFileDatabase.server.queryResult;

import org.jetbrains.annotations.NotNull;
import sqlapi.queryResult.QueryResultRow;
import sqlapi.queryResult.QueryResult;

import java.util.List;

public final class QueryResultImpl implements QueryResult {

    private final List<QueryResultRow> rows;

    private final List<String> columns;

    public QueryResultImpl(List<QueryResultRow> rows, List<String> columns) {
        this.rows = rows;
        this.columns = columns;
    }

    @NotNull
    @Override
    public List<String> getHeaders() {
        return columns;
    }

    @NotNull
    @Override
    public List<QueryResultRow> getRows() {
        return rows;
    }
}
