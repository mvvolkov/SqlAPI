package api.selectedItems;

import api.columnExpr.ColumnExpression;

public interface SelectedColumnExpression extends SelectedItem {

    ColumnExpression getColumnExpression();

    String getAlias();

    @Override
    default Type getType() {
        return Type.SELECT_COLUMN_EXPRESSION;
    }
}
