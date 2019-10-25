package SimpleFileImpl;

import sqlapi.exceptions.NoSuchColumnException;
import sqlapi.selectionResult.ResultRow;
import sqlapi.selectionResult.ResultValue;

import java.util.List;

public class ResultRowImpl implements ResultRow {

    private final List<ResultValueImpl> values;

    public ResultRowImpl(List<ResultValueImpl> values) {
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
        for (ResultValueImpl value : values) {
            if (value.getColumnName().equals(columnName)) {
                return value;
            }
        }
        return null;
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
