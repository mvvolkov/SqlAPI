package serverLocalFileImpl;

import api.exceptions.NoSuchColumnException;
import api.selectResult.ResultRow;

import java.util.List;
import java.util.Map;

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
