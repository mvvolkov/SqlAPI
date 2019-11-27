package clientImpl.predicates;

import sqlapi.columnExpr.ColumnRef;
import sqlapi.columnExpr.ColumnValue;
import sqlapi.predicates.ColumnInPredicate;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

final class ColumnInPredicateImpl extends PredicateImpl
        implements ColumnInPredicate {

    @NotNull
    private final ColumnRef columnRef;

    @NotNull
    private final List<ColumnValue> values;

    ColumnInPredicateImpl(@NotNull ColumnRef columnRef,
                          @NotNull List<ColumnValue> values) {
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
    public List<ColumnValue> getColumnValues() {
        return values;
    }

    @Override
    public String toString() {
        return columnRef + " IN (" + values.stream().map(ColumnValue::toString).collect(
                Collectors.joining(", ")) + ")";
    }
}
