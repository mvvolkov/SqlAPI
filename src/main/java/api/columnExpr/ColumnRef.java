package api.columnExpr;

public interface ColumnRef extends ColumnExpression {

    String getColumnName();

    String getTableName();

    String getSchemaName();

    @Override default String getAlias() {
        return getColumnName();
    }

    @Override
    default ExprType getExprType() {
        return ExprType.COLUMN_REF;
    }
}
