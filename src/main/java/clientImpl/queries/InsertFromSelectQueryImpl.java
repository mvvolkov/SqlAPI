package clientImpl.queries;

import sqlapi.queries.InsertFromSelectQuery;
import sqlapi.queries.SelectQuery;
import org.jetbrains.annotations.NotNull;

import java.util.List;

final class InsertFromSelectQueryImpl extends AbstractSqlTableQueryImpl implements
        InsertFromSelectQuery {

    @NotNull
    private final List<String> columns;

    @NotNull
    private final SelectQuery selectQuery;

    InsertFromSelectQueryImpl(@NotNull String databaseName, @NotNull String tableName,
                              @NotNull List<String> columns,
                              @NotNull SelectQuery selectQuery) {
        super(databaseName, tableName);
        this.columns = columns;
        this.selectQuery = selectQuery;
    }

    @NotNull @Override
    public List<String> getColumns() {
        return columns;
    }

    @NotNull @Override
    public SelectQuery getSelectQuery() {
        return selectQuery;
    }
}
