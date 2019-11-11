package serverLocalFileImpl;

import api.columnExpr.ColumnRef;

import java.util.List;

public class InternalResultSet {


    private final List<ColumnRef> columns;
    private final List<InternalResultRow> rows;

    public InternalResultSet(List<ColumnRef> columns, List<InternalResultRow> rows) {
        this.columns = columns;
        this.rows = rows;
    }

    public void addColumns(List<ColumnRef> columns) {
        this.columns.addAll(columns);
    }

    public void addRows(List<InternalResultRow> rows) {
        this.rows.addAll(rows);
    }

    public void addRow(InternalResultRow row) {
        this.rows.add(row);
    }

    public List<ColumnRef> getColumns() {
        return columns;
    }

    public List<InternalResultRow> getRows() {
        return rows;
    }

}
