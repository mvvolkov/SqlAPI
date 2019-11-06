package clientDefaultImpl;

import api.selectionPredicate.ColumnColumnPredicate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ColumnColumnsPredicateImpl extends SelectionPredicateImpl implements ColumnColumnPredicate {


    @NotNull
    private final ColumnReferenceImpl leftColumn;

    @NotNull
    private final ColumnReferenceImpl rightColumn;


    public ColumnColumnsPredicateImpl(@NotNull Type type, @NotNull ColumnReferenceImpl leftColumn,
                                      @Nullable ColumnReferenceImpl rightColumn) {
        super(type);
        this.leftColumn = leftColumn;
        this.rightColumn = rightColumn;
    }

    @NotNull
    @Override
    public ColumnReferenceImpl getLeftColumn() {
        return leftColumn;
    }

    @NotNull
    @Override
    public ColumnReferenceImpl getRightColumn() {
        return rightColumn;
    }

    @Override
    public String toString() {
        return leftColumn + " " + this.getOperatorString() + " " + rightColumn;
    }
}
