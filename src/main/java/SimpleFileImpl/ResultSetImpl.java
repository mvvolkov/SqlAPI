package SimpleFileImpl;

import sqlapi.dbMetadata.ColumnMetadata;
import sqlapi.exceptions.NoSuchColumnException;
import sqlapi.selectionResult.ResultRow;
import sqlapi.selectionResult.ResultSet;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ResultSetImpl implements ResultSet {

    private final List<ResultRow> rows;

    private final List<ColumnMetadata> columns;

    public ResultSetImpl(List<ResultRow> rows, List<ColumnMetadata> columns) {

        this.rows = rows;
        this.columns = columns;
    }

    @Override
    public List<ColumnMetadata> getColumns() {
        return null;
    }

    @Override
    public List<ResultRow> getAllRows() {
        return rows;
    }

    @Override
    public List<ResultRow> getFirstRows(int numberOfRows) {
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        String tableString = columns.stream().map(ColumnMetadata::getColumnName)
                .collect(Collectors.joining(", "));

        sb.append("\nSelect result : ");
        sb.append(tableString);
        for (ResultRow row : rows) {
            List<String> values = new ArrayList<>();
            for (ColumnMetadata column : columns) {
                String value;
                try {
                    value = row.getValue(column.getColumnName()).toString();
                } catch (NoSuchColumnException e) {
                    value = e.getMessage();
                }
                values.add(value);
            }
            String rowString = values.stream().collect(Collectors.joining(", "));
            sb.append("\n" + rowString);
        }
        return sb.toString();
    }
}
