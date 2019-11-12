package serverLocalFileImpl;

import api.columnExpr.ColumnRef;

import java.util.List;

public final class InternalResultRow {


    private final List<ColumnRef> columns;
    private final List<Object> values;

    public InternalResultRow(List<ColumnRef> columns, List<Object> values) {
        this.columns = columns;
        this.values = values;
    }


    public void addValues(List<Object> values) {
        this.values.addAll(values);
    }

    public void addValue(Object value) {
        this.values.add(value);
    }

    public List<Object> getValues() {
        return values;
    }

    public void addColumns(List<ColumnRef> values) {
        this.columns.addAll(values);
    }

    public void addColumn(ColumnRef value) {
        this.columns.add(value);
    }

    public List<ColumnRef> getColumns() {
        return columns;
    }
}
