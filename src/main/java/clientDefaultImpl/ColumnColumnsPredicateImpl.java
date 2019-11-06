package clientDefaultImpl;

import api.selectionPredicate.ColumnColumnPredicate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import api.ColumnReference;

public class ColumnColumnsPredicateImpl extends SelectionPredicateImpl implements ColumnColumnPredicate {


    @NotNull
    private final ColumnReference leftColumn;

    @NotNull
    private final ColumnReference rightColumn;


    public ColumnColumnsPredicateImpl(@NotNull Type type, @NotNull ColumnReference leftColumn,
                                      @Nullable ColumnReference rightColumn) {
        super(type);
        this.leftColumn = leftColumn;
        this.rightColumn = rightColumn;
    }

    @NotNull
    @Override
    public ColumnReference getLeftColumn() {
        return leftColumn;
    }

    @NotNull
    @Override
    public ColumnReference getRightColumn() {
        return rightColumn;
    }

    @Override
    public String toString() {
        return leftColumn + " " + this.getOperatorString() + " " + rightColumn;
    }
}
