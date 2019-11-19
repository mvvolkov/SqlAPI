package serverLocalFileImpl;

import sqlapi.selectResult.ResultRow;

import java.util.List;

public final class ResultRowImpl implements ResultRow {

    private final List<Object> values;

    public ResultRowImpl(List<Object> values) {
        this.values = values;
    }


    @Override
    public Object getObject(int index) {
        return values.get(index);
    }

    @Override public List<Object> getValues() {
        return values;
    }

}
