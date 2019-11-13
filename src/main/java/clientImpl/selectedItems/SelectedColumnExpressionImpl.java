package clientImpl.selectedItems;

import api.columnExpr.ColumnExpression;
import api.selectedItems.SelectedColumnExpression;

public class SelectedColumnExpressionImpl extends SelectedItemImpl implements SelectedColumnExpression {

    private final ColumnExpression expression;
    private final String alias;

    public SelectedColumnExpressionImpl(ColumnExpression expression, String alias) {
        super(Type.SELECT_COLUMN_EXPRESSION);
        this.expression = expression;
        this.alias = alias;
    }


    @Override
    public String toString() {
        return "";
    }

    @Override
    public ColumnExpression getColumnExpression() {
        return expression;
    }

    @Override
    public String getAlias() {
        return alias;
    }
}
