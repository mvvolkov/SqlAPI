package clientImpl.selectedItems;

import api.columnExpr.ColumnExpression;

public class SelectedColumnExpressionImpl extends SelectedItemImpl {

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
}
