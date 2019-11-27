package sqlapi.columnExpr;

import sqlapi.assignment.SelectedItem;
import org.jetbrains.annotations.NotNull;

public interface ColumnExpression extends SelectedItem {

    @NotNull
    default String getAlias() {
        return "";
    }

    @NotNull ColumnExpression add(@NotNull ColumnExpression otherExpression);

    @NotNull ColumnExpression add(@NotNull ColumnExpression otherExpression, @NotNull String alias);

    @NotNull ColumnExpression subtract(@NotNull ColumnExpression otherExpression);

    @NotNull ColumnExpression subtract(@NotNull ColumnExpression otherExpression, @NotNull String alias);

    @NotNull ColumnExpression multiply(@NotNull ColumnExpression otherExpression);

    @NotNull ColumnExpression multiply(@NotNull ColumnExpression otherExpression, @NotNull String alias);

    @NotNull ColumnExpression divide(@NotNull ColumnExpression otherExpression);

    @NotNull ColumnExpression divide(@NotNull ColumnExpression otherExpression, @NotNull String alias);
}
