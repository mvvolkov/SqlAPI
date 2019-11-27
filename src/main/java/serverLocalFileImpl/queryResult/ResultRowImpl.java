package serverLocalFileImpl.queryResult;

import org.jetbrains.annotations.NotNull;
import sqlapi.queryResult.ResultRow;

import java.util.List;

public final class ResultRowImpl implements ResultRow {

    private final List<Object> values;

    public ResultRowImpl(List<Object> values) {
        this.values = values;
    }


    @Override
    public Object getValue(int index) {
        return values.get(index);
    }

    @NotNull
    @Override public List<Object> getValues() {
        return values;
    }

}
