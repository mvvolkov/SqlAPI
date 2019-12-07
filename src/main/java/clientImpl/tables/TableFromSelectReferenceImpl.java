package clientImpl.tables;

import org.jetbrains.annotations.NotNull;
import sqlapi.queries.SelectQuery;
import sqlapi.tables.TableFromSelectReference;

final class TableFromSelectReferenceImpl implements TableFromSelectReference {

    private final SelectQuery selectQuery;

    private final String alias;


    TableFromSelectReferenceImpl(SelectQuery selectQuery, String alias) {
        this.selectQuery = selectQuery;
        this.alias = alias;
    }

    @NotNull
    @Override
    public String getAlias() {
        return alias;
    }

    @NotNull
    @Override
    public SelectQuery getSelectQuery() {
        return selectQuery;
    }


    @Override public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append(selectQuery);
        sb.append(")");
        if (!alias.isEmpty()) {
            sb.append(" ");
            sb.append(alias);
        }
        return sb.toString();
    }
}
