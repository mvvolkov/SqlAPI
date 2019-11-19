package sqlapi.columnExpr;

public interface AggregateFunction extends ColumnExpression {

    enum Type {
        COUNT,
        SUM,
        AVG,
        MAX,
        MIN
    }

    @Override
    default ExprType getExprType() {
        return ExprType.AGGR_FUNC;
    }

    ColumnRef getColumnRef();

    String getAlias();

    Type getType();
}
