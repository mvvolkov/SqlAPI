package clientImpl.queries;

import org.jetbrains.annotations.NotNull;
import sqlapi.queries.InsertFromSelectQuery;
import sqlapi.queries.SelectQuery;

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

    @Override public String toString() {
        StringBuilder sb = new StringBuilder("INSERT INTO ");
        sb.append(this.getDatabaseName());
        sb.append(".");
        sb.append(this.getTableName());
        if (!this.getColumns().isEmpty()) {
            sb.append("(");
            sb.append(String.join(", ", this.getColumns()));
            sb.append(")");
        }
        sb.append(" ").append(selectQuery).append(";");
        return sb.toString();
    }
}
