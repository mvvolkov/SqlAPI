package serverLocalFileImpl;

import api.exceptions.NoSuchColumnException;
import api.selectResult.ResultRow;

import java.util.List;

public final class ResultRowImpl implements ResultRow {


    private final List<String> columns;
    private final List<Object> values;

    public ResultRowImpl(List<String> columns, List<Object> values) {
        this.columns = columns;
        this.values = values;
    }

    private int getColumnIndex(String columnName) throws NoSuchColumnException {
        int index = columns.indexOf(columnName);
        if (index == -1) {
            throw new NoSuchColumnException(columnName);
        }
        return index;
    }


    @Override
    public List<Object> getValues() {
        return values;
    }

    @Override
    public Integer getInteger(String columnName) throws NoSuchColumnException {
        Object value = this.getObject(columnName);
        return (Integer) value;
    }

    @Override
    public Integer getInteger(int index) {
        Object value = this.getObject(index);
        return (Integer) value;
    }

    @Override
    public String getString(String columnName) throws NoSuchColumnException {
        Object value = this.getObject(columnName);
        return (String) value;
    }

    @Override
    public String getString(int index) {
        Object value = this.getObject(index);
        return (String) value;
    }

    @Override
    public Object getObject(String columnName) throws NoSuchColumnException {
        return values.get(this.getColumnIndex(columnName));
    }

    @Override
    public Object getObject(int index) {
        return values.get(index);
    }

    @Override
    public boolean isNull(String columnName) throws NoSuchColumnException {
        return this.getObject(columnName) == null;
    }

    @Override
    public boolean isNull(int index) {
        return this.getObject(index) == null;
    }
}
