package sqlapi.columnExpr;

import sqlapi.misc.SelectedItem;
import org.jetbrains.annotations.NotNull;

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

    @NotNull
    default String getAlias() {
        return "";
    }

    ExprType getExprType();

}
