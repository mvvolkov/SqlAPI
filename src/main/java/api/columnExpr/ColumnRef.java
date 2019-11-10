package api.columnExpr;

public interface ColumnRef extends ColumnExpression {

    String getColumnName();

    String getTableName();

    String getDatabaseName();

    @Override default Type getType() {
        return Type.COLUMN_REF;
    }
}
