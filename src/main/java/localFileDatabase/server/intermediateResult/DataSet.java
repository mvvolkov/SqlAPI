package localFileDatabase.server.intermediateResult;

import java.util.*;

public final class DataSet {


    private final List<DataHeader> columns;

    private final Collection<DataRow> rows;

    public DataSet(List<DataHeader> columns, Collection<DataRow> rows) {
        this.columns = Collections.unmodifiableList(columns);
        this.rows = Collections.unmodifiableCollection(rows);
    }


    public List<DataHeader> getColumns() {
        return columns;
    }

    public Collection<DataRow> getRows() {
        return rows;
    }

    public DataSet productWith(DataSet otherSet) {

        List<DataHeader> newColumns = new ArrayList<>(columns);
        newColumns.addAll(otherSet.getColumns());
        List<DataRow> newRows = new ArrayList<>();
        for (DataRow row : rows) {
            for (DataRow otherRow : otherSet.getRows()) {
                List<DataValue> newValues = new ArrayList<>(row.getValues());
                newValues.addAll(otherRow.getValues());
                newRows.add(new DataRow(newValues));
            }
        }
        return new DataSet(newColumns, newRows);
    }


}
