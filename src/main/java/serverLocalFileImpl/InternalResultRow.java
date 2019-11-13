package serverLocalFileImpl;

import api.columnExpr.ColumnRef;

import java.util.Map;

public final class InternalResultRow {


    //    private final List<ColumnRef> columns;
    private final Map<ColumnRef, Object> values;

    public InternalResultRow(Map<ColumnRef, Object> values) {
//        this.columns = columns;
        this.values = values;
    }


//    public void addValues(List<Object> values) {
//        this.values.addAll(values);
//    }
//
//    public void addValue(Object value) {
//        this.values.add(value);
//    }

    public Map<ColumnRef, Object> getValues() {
        return values;
    }

//    public void addColumns(List<ColumnRef> values) {
//        this.columns.addAll(values);
//    }
//
//    public void addColumn(ColumnRef value) {
//        this.columns.add(value);
//    }

//    public List<ColumnRef> getColumns() {
//        return columns;
//    }
}
