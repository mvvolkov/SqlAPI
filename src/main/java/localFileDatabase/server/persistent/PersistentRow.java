package localFileDatabase.server.persistent;

import java.io.Serializable;
import java.util.Map;

public final class PersistentRow implements Serializable {

    public static final long serialVersionUID = 8515497022392723148L;

    private final Map<String, Object> values;

    public PersistentRow(Map<String, Object> values) {
        this.values = values;
    }

    public Map<String, Object> getValues() {
        return values;
    }

    public Object getValue(String columnName) {
        return values.get(columnName);
    }
}
