package serverLocalFileImpl.queryResult;

import sqlapi.selectResult.ResultRow;
import sqlapi.selectResult.ResultSet;

import java.util.List;

public final class ResultSetImpl implements ResultSet {

    private final List<ResultRow> rows;

    private final List<String> columns;

    public ResultSetImpl(List<ResultRow> rows, List<String> columns) {
        this.rows = rows;
        this.columns = columns;
    }

    @Override
    public List<String> getHeaders() {
        return columns;
    }

    @Override
    public List<ResultRow> getRows() {
        return rows;
    }
}
