package serverLocalFileImpl;

import api.exceptions.NoSuchColumnException;
import api.selectResult.ResultRow;

import java.util.Map;

public final class ResultRowImpl implements ResultRow {


    private final Map<String, Object> values;

    public ResultRowImpl(Map<String, Object> values) {
        this.values = values;
    }


    @Override
    public Integer getInteger(String columnName) throws NoSuchColumnException {
        Object value = this.getObject(columnName);
        return (Integer) value;
    }

    @Override
    public String getString(String columnName) throws NoSuchColumnException {
        Object value = this.getObject(columnName);
        return (String) value;
    }

    @Override
    public Object getObject(String columnName) throws NoSuchColumnException {
        if (!values.containsKey(columnName)) {
            throw new NoSuchColumnException(columnName);
        }
        return values.get(columnName);
    }

    @Override
    public boolean isNull(String columnName) throws NoSuchColumnException {
        return this.getObject(columnName) == null;
    }

}
