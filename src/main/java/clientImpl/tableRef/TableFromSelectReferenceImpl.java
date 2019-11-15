package clientImpl.tableRef;

import api.queries.SelectExpression;
import api.tables.TableFromSelectReference;

public class TableFromSelectReferenceImpl implements TableFromSelectReference {

    private final SelectExpression selectExpression;

    private final String alias;


    public TableFromSelectReferenceImpl(SelectExpression selectExpression, String alias) {
        this.selectExpression = selectExpression;
        this.alias = alias;
    }

    @Override
    public String getAlias() {
        return alias;
    }

    @Override
    public SelectExpression getSelectExpression() {
        return selectExpression;
    }
}
