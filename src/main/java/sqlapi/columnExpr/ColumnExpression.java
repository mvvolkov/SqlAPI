package sqlapi.columnExpr;

import org.jetbrains.annotations.NotNull;
import sqlapi.misc.SelectedItem;

import java.util.List;

public interface ColumnExpression extends SelectedItem {

    @NotNull
    default String getAlias() {
        return "";
    }

    default void setParameters(List<Object> parameters) {
    }

    @NotNull ColumnExpression add(@NotNull ColumnExpression otherExpression);

    @NotNull ColumnExpression add(@NotNull ColumnExpression otherExpression,
                                  @NotNull String alias);

    @NotNull ColumnExpression subtract(@NotNull ColumnExpression otherExpression);

    @NotNull ColumnExpression subtract(@NotNull ColumnExpression otherExpression,
                                       @NotNull String alias);

    @NotNull ColumnExpression multiply(@NotNull ColumnExpression otherExpression);

    @NotNull ColumnExpression multiply(@NotNull ColumnExpression otherExpression,
                                       @NotNull String alias);

    @NotNull ColumnExpression divide(@NotNull ColumnExpression otherExpression);

    @NotNull ColumnExpression divide(@NotNull ColumnExpression otherExpression,
                                     @NotNull String alias);
}
