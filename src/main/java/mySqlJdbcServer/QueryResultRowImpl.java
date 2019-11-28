package mySqlJdbcServer;

import org.jetbrains.annotations.NotNull;
import sqlapi.queryResult.QueryResultRow;

import java.util.List;

public final class QueryResultRowImpl implements QueryResultRow {

    private final List<Object> values;

    public QueryResultRowImpl(List<Object> values) {
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
