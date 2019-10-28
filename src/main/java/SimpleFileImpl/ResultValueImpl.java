package SimpleFileImpl;

import sqlapi.selectionResult.ResultValue;

public class ResultValueImpl implements ResultValue {

    private final Value value;

    private final String columnName;

    public ResultValueImpl(Value value, String columnName) {
        this.value = value;
        this.columnName = columnName;
    }

    @Override
    public String getColumnName() {
        return columnName;
    }

    @Override
    public Integer getInteger() {
        return null;
    }

    @Override
    public String getString() {
        return null;
    }

    @Override
    public <T> Comparable<T> getValue() {
        return value.getValue();
    }

    @Override
    public boolean isNull() {
        return value.isNull();
    }

    @Override
    public boolean isNotNull() {
        return value.isNotNull();
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
