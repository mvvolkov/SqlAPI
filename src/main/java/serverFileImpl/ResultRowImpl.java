package serverFileImpl;

import api.exceptions.NoSuchColumnException;
import api.selectionResult.ResultRow;
import api.selectionResult.ResultValue;

import java.util.List;

public class ResultRowImpl implements ResultRow {


    private final List<ResultValue> values;

    public ResultRowImpl(List<ResultValue> values) {
        this.values = values;
    }


    @Override
    public ResultValue getValue(String columnName) throws NoSuchColumnException {
        ResultValue value = this.getValueOrNull(columnName);
        if (value == null) {
            throw new NoSuchColumnException(columnName);
        }
        return value;
    }

    @Override
    public ResultValue getValueOrNull(String columnName) {
        for (ResultValue value : values) {
            if (value.getColumnName().equals(columnName)) {
                return value;
            }
        }
        return null;
    }

    public List<ResultValue> getValues() {
        return values;
    }


    @Override
    public ResultValue getValue(int index) {
        return null;
    }

    @Override
    public ResultValue getValueOrNull(int index) {
        return null;
    }

    @Override
    public int getLength() {
        return values.size();
    }

    @Override
    public int findColumn(String columnName) {
        return 0;
    }
}
