package api.columnExpr;

public interface ColumnRef extends ColumnExpression {

    String getColumnName();

    String getTableName();

    String getDatabaseName();

    @Override default String getAlias() {
        return getColumnName();
    }

    @Override
    default ExprType getExprType() {
        return ExprType.COLUMN_REF;
    }
}
