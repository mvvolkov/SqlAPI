package clientImpl.predicates;

import org.jetbrains.annotations.NotNull;
import sqlapi.columnExpr.ColumnRef;
import sqlapi.columnExpr.InputValue;
import sqlapi.predicates.ColumnInPredicate;

import java.util.ArrayDeque;
import java.util.List;
import java.util.stream.Collectors;

final class ColumnInPredicateImpl extends PredicateImpl
        implements ColumnInPredicate {

    @NotNull
    private final ColumnRef columnRef;

    @NotNull
    private final List<InputValue> values;

    ColumnInPredicateImpl(@NotNull ColumnRef columnRef,
                          @NotNull List<InputValue> values) {
        this.columnRef = columnRef;
        this.values = values;
    }


    @NotNull
    @Override
    public ColumnRef getColumnRef() {
        return columnRef;
    }

    @NotNull
    @Override
    public List<InputValue> getColumnValues() {
        return values;
    }

    @Override
    public String toString() {
        return columnRef + " IN (" + values.stream().map(InputValue::toString).collect(
                Collectors.joining(", ")) + ")";
    }

}
