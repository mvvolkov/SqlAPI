package serverLocalFileImpl;

import api.columnExpr.ColumnRef;

import java.util.Map;

public final class InternalResultRow {

    private final Map<ColumnRef, Object> values;

    public InternalResultRow(Map<ColumnRef, Object> values) {
        this.values = values;
    }

    public Map<ColumnRef, Object> getValues() {
        return values;
    }
}
