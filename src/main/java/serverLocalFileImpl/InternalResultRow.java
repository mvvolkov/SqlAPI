package serverLocalFileImpl;

import api.columnExpr.ColumnRef;

import java.util.List;

public class InternalResultRow {


    private final List<ColumnRef> columns;
    private final List<Value> values;

    public InternalResultRow(List<ColumnRef> columns, List<Value> values) {
        this.columns = columns;
        this.values = values;
    }


    public void addValues(List<Value> values) {
        this.values.addAll(values);
    }

    public void addValue(Value value) {
        this.values.add(value);
    }

    public List<Value> getValues() {
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
