package clientImpl.columnExpr;

import api.columnExpr.ColumnExpression;
import api.columnExpr.ColumnRef;
import api.selectedItems.SelectedColumnExpression;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ColumnRefImpl extends ColumnExprImpl implements ColumnRef, SelectedColumnExpression {

    @NotNull
    private final String columnName;
    @Nullable
    private final String tableName;
    @Nullable
    private final String databaseName;

    public ColumnRefImpl(String databaseName, String tableName, String columnName) {
        super(ExprType.COLUMN_REF);
        this.databaseName = databaseName;
        this.tableName = tableName;
        this.columnName = columnName;
    }

    public ColumnRefImpl(String tableName, String columnName) {
        this(null, tableName, columnName);
    }

    public ColumnRefImpl(String columnName) {
        this(null, columnName);
    }


    @Override
    public String getColumnName() {
        return columnName;
    }

    @Override
    public String getTableName() {
        return tableName;
    }

    @Override
    public String getDatabaseName() {
        return databaseName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(columnName);
        if (tableName != null) {
            sb.insert(0, ".");
            sb.insert(0, tableName);
        }
        if (databaseName != null) {
            sb.insert(0, ".");
            sb.insert(0, databaseName);
        }
        return sb.toString();
    }

    @Override
    public ColumnExpression getColumnExpression() {
        return this;
    }

    @Override
    public String getAlias() {
        return this.getColumnName();
    }
}
