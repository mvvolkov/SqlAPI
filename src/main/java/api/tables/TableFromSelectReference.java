package api.tables;

import api.queries.SelectExpression;

public interface TableFromSelectReference extends TableReference {

    String getAlias();

    SelectExpression getSelectExpression();

    @Override
    default TableRefType getTableRefType() {
        return TableRefType.TABLE_FROM_SELECT;
    }
}
