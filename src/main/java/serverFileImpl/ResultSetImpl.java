package serverFileImpl;

import api.ResultRow;
import api.ResultSet;

import java.util.List;

public class ResultSetImpl implements ResultSet {

    private final List<ResultRow> rows;

    private final List<String> columns;

    public ResultSetImpl(List<ResultRow> rows, List<String> columns) {
        this.rows = rows;
        this.columns = columns;
    }

    @Override
    public List<String> getColumns() {
        return columns;
    }

    @Override
    public List<ResultRow> getRows() {
        return rows;
    }
}
