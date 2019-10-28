package sqlapi.selectionPredicate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sqlapi.ColumnReference;

public class TwoColumnsPredicate extends SelectionPredicate {


    @NotNull
    private final ColumnReference leftColumn;

    @NotNull
    private final ColumnReference rightColumn;


    public TwoColumnsPredicate(@NotNull Type type, @NotNull ColumnReference leftColumn,
                               @Nullable ColumnReference rightColumn) {
        super(type);
        this.leftColumn = leftColumn;
        this.rightColumn = rightColumn;
    }

    @NotNull
    public ColumnReference getLeftColumn() {
        return leftColumn;
    }

    @NotNull
    public ColumnReference getRightColumn() {
        return rightColumn;
    }

    @Override
    public String toString() {
        return leftColumn + " " + this.getOperatorString() + " " + rightColumn;
    }
}
