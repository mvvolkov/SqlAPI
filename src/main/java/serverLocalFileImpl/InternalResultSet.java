package serverLocalFileImpl;

import api.columnExpr.ColumnRef;

import java.util.*;

public final class InternalResultSet {


    private final List<ColumnRef> columns;
    private final List<InternalResultRow> rows;

    public InternalResultSet(List<ColumnRef> columns, List<InternalResultRow> rows) {
        this.columns = columns;
        this.rows = rows;
    }


    public List<ColumnRef> getColumns() {
        return columns;
    }

    public List<InternalResultRow> getRows() {
        return rows;
    }

    public InternalResultSet joinWith(InternalResultSet otherSet) {

        List<ColumnRef> newColumns = new ArrayList<>(columns);
        newColumns.addAll(otherSet.getColumns());
        List<InternalResultRow> newRows = new ArrayList<>();
        for (InternalResultRow row : rows) {
            for (InternalResultRow otherRow : otherSet.getRows()) {
                Map<ColumnRef, Object> newValues = new HashMap<>(row.getValues());
                newValues.putAll(otherRow.getValues());
                newRows.add(new InternalResultRow(newValues));
            }
        }
        return new InternalResultSet(newColumns, newRows);
    }

}
