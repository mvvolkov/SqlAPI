package serverLocalFileImpl.intermediateresult;

import java.util.*;

public final class DataSet {


    private final List<DataHeader> columns;
    private final List<DataRow> rows;

    public DataSet(List<DataHeader> columns, List<DataRow> rows) {
        this.columns = columns;
        this.rows = rows;
    }


    public List<DataHeader> getColumns() {
        return columns;
    }

    public List<DataRow> getRows() {
        return rows;
    }

    public DataSet joinWith(DataSet otherSet) {

        List<DataHeader> newColumns = new ArrayList<>(columns);
        newColumns.addAll(otherSet.getColumns());
        List<DataRow> newRows = new ArrayList<>();
        for (DataRow row : rows) {
            for (DataRow otherRow : otherSet.getRows()) {
                Map<DataHeader, Object> newValues = new HashMap<>(row.getCells());
                newValues.putAll(otherRow.getCells());
                newRows.add(new DataRow(newValues));
            }
        }
        return new DataSet(newColumns, newRows);
    }

}
