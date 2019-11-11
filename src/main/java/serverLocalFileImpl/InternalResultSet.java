package serverLocalFileImpl;

import java.util.List;

public class InternalResultSet {


    private final List<ColumnRefImpl> columns;
    private final List<InternalResultRow> rows;

    public InternalResultSet(List<ColumnRefImpl> columns, List<InternalResultRow> rows) {
        this.columns = columns;
        this.rows = rows;
    }

    public void addColumns(List<ColumnRefImpl> columns) {
        this.columns.addAll(columns);
    }

    public void addRows(List<InternalResultRow> rows) {
        this.rows.addAll(rows);
    }

    public void addRow(InternalResultRow row) {
        this.rows.add(row);
    }

    public List<ColumnRefImpl> getColumns() {
        return columns;
    }

    public List<InternalResultRow> getRows() {
        return rows;
    }

}
