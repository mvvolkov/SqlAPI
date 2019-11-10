package clientImpl.predicates;

import api.columnExpr.ColumnRef;
import api.predicates.ColumnColumnPredicate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ColumnColumnsPredicateImpl extends SelectionPredicateImpl
        implements ColumnColumnPredicate {


    @NotNull
    private final ColumnRef leftColumn;

    @NotNull
    private final ColumnRef rightColumn;


    public ColumnColumnsPredicateImpl(@NotNull Type type,
                                      @NotNull ColumnRef leftColumn,
                                      @Nullable ColumnRef rightColumn) {
        super(type);
        this.leftColumn = leftColumn;
        this.rightColumn = rightColumn;
    }

    @NotNull
    @Override
    public ColumnRef getLeftColumnRef() {
        return leftColumn;
    }

    @NotNull
    @Override
    public ColumnRef getRightColumnRef() {
        return rightColumn;
    }

    @Override
    public String toString() {
        return leftColumn + " " + this.getOperatorString() + " " + rightColumn;
    }
}
