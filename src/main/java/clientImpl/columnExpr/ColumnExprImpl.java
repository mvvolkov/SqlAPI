package clientImpl.columnExpr;

import api.columnExpr.ColumnExpression;

public class ColumnExprImpl implements ColumnExpression {


    private ExprType exprType;

    protected ColumnExprImpl(ExprType exprType) {
        this.exprType = exprType;
    }

    @Override
    public ExprType getExprType() {
        return exprType;
    }

}
