package clientImpl.columnExpr;

import api.columnExpr.ColumnExpression;

public class ColumnExprImpl implements ColumnExpression {


    private Type type;

    protected ColumnExprImpl(Type type) {
        this.type = type;
    }

    @Override
    public Type getType() {
        return type;
    }

}
