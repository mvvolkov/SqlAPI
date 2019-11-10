package clientImpl.selectedItems;

import api.SelectedItem;
import api.columnExpr.ColumnExpression;

public class SelectedItemFactory {

    private SelectedItemFactory() {
    }

    public static SelectedItem all() {
        return new SelectedAllImpl();
    }

    public static SelectedItem allFromTable(String tableName) {
        return new SelectedTableImpl(tableName);
    }
    
    public static SelectedItem getColumnExpression(ColumnExpression columnExpression) {
        return new SelectedColumnExpressionImpl(columnExpression, null);
    }
}
