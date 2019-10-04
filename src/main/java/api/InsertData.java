package api;

import java.util.List;

public class InsertData {

    private final List<String> columns;
    private final List<Object> values;

    public InsertData(List<String> columns, List<Object> values) {
        this.columns = columns;
        this.values = values;
    }

    public InsertData(List<Object> values) {
        this.columns = null;
        this.values = values;
    }

    public boolean hasColumnsList(){
        return columns != null;
    }

    public List<String> getColumns() {
        return columns;
    }

    public List<Object> getValues() {
        return values;
    }
}
