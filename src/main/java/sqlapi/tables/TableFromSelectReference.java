package sqlapi.tables;

import sqlapi.queries.SelectExpression;

public interface TableFromSelectReference extends TableReference {

    String getAlias();

    SelectExpression getSelectExpression();

    @Override
    default TableRefType getTableRefType() {
        return TableRefType.SELECT_SUBQUERY;
    }
}
