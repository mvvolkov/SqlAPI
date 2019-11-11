package serverLocalFileImpl;

import java.util.List;

public class InternalResultRow {


    private final List<ColumnRefImpl> columns;
    private final List<Value> values;

    public InternalResultRow(List<ColumnRefImpl> columns, List<Value> values) {
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

    public void addColumns(List<ColumnRefImpl> values) {
        this.columns.addAll(values);
    }

    public void addColumn(ColumnRefImpl value) {
        this.columns.add(value);
    }

    public List<ColumnRefImpl> getColumns() {
        return columns;
    }
}
