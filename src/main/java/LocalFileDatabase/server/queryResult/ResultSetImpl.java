package LocalFileDatabase.server.queryResult;

import org.jetbrains.annotations.NotNull;
import sqlapi.queryResult.ResultRow;
import sqlapi.queryResult.ResultSet;

import java.util.List;

public final class ResultSetImpl implements ResultSet {

    private final List<ResultRow> rows;

    private final List<String> columns;

    public ResultSetImpl(List<ResultRow> rows, List<String> columns) {
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
    public List<ResultRow> getRows() {
        return rows;
    }
}
