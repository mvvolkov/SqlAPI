package sqlapi.tables;

import sqlapi.queries.SelectQuery;

public interface TableFromSelectReference extends TableReference {

    String getAlias();

    SelectQuery getSelectQuery();

    @Override
    default TableRefType getTableRefType() {
        return TableRefType.SELECT_SUBQUERY;
    }
}
