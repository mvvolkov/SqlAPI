package api.columnExpr;

import api.misc.SelectedItem;

public interface ColumnExpression extends SelectedItem {

    enum ExprType {
        COLUMN_VALUE,
        COLUMN_REF,
        NULL_VALUE,
        AGGR_FUNC,
        SUM,
        DIFF,
        PRODUCT,
        DIVIDE
    }

    default String getAlias() {
        return "";
    }

    ExprType getExprType();
}
