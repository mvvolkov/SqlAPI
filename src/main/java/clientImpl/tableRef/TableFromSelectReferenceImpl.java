package clientImpl.tableRef;

import sqlapi.queries.SelectQuery;
import sqlapi.tables.TableFromSelectReference;

public class TableFromSelectReferenceImpl implements TableFromSelectReference {

    private final SelectQuery selectQuery;

    private final String alias;


    public TableFromSelectReferenceImpl(SelectQuery selectQuery, String alias) {
        this.selectQuery = selectQuery;
        this.alias = alias;
    }

    @Override
    public String getAlias() {
        return alias;
    }

    @Override
    public SelectQuery getSelectQuery() {
        return selectQuery;
    }
}
