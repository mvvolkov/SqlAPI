package clientImpl.selectedItems;

import api.columnExpr.ColumnRef;
import api.selectedItems.SelectedItem;
import api.columnExpr.ColumnExpression;

public class SelectedItemFactory {

    private SelectedItemFactory() {
    }

    public static SelectedItem all() {
        return new SelectedAllImpl();
    }

    public static SelectedItem allFromTable(String dbName, String tableName) {
        return new SelectedTableImpl(dbName, tableName);
    }

    public static SelectedItem columnExpression(ColumnExpression columnExpression, String alias) {
        return new SelectedColumnExpressionImpl(columnExpression, alias);
    }

    public static SelectedItem column(ColumnRef columnRef) {
        return new SelectedColumnExpressionImpl(columnRef, columnRef.getColumnName());
    }
}
