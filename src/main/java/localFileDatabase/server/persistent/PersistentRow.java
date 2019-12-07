package localFileDatabase.server.persistent;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public final class PersistentRow implements Serializable {

    public static final long serialVersionUID = 8515497022392723148L;

    private final Map<String, Object> values;

    PersistentRow() {
        this.values = new HashMap<>();
    }

    public Object getValue(String columnName) {
        return values.get(columnName);
    }

    boolean hasValue(String columnName) {
        return values.containsKey(columnName);
    }

    void setValue(String columnName, Object value) {
        values.put(columnName, value);
    }

    int length() {
        return values.size();
    }
}
