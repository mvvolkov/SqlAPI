package serverLocalFileImpl;

import java.io.Serializable;
import java.util.Map;

public final class Row implements Serializable {


    private final Map<String, Value> values;

    public Row(Map<String, Value> values) {
        this.values = values;
    }


    public Map<String, Value> getValues() {
        return values;
    }


    public Value getValue(String columnName) {
        return values.get(columnName);
    }
}
