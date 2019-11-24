package clientImpl.queries;

import sqlapi.queries.InsertQuery;
import org.jetbrains.annotations.NotNull;

import java.util.List;

final class InsertQueryImpl extends AbstractSqlTableQueryImpl implements InsertQuery {

    @NotNull
    private final List<String> columns;

    @NotNull
    private final List<Object> values;

    InsertQueryImpl(String databaseName, String tableName,
                           List<String> columns, List<Object> values) {
        super(databaseName, tableName);
        this.columns = columns;
        this.values = values;
    }


    @NotNull @Override
    public List<Object> getValues() {
        return values;
    }

    @NotNull @Override
    public List<String> getColumns() {
        return columns;
    }
}
