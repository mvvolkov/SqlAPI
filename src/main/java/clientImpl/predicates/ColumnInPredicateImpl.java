package clientImpl.predicates;

import api.columnExpr.ColumnRef;
import api.columnExpr.ColumnValue;
import api.predicates.ColumnInPredicate;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public final class ColumnInPredicateImpl extends PredicateImpl
        implements ColumnInPredicate {

    @NotNull
    private final ColumnRef columnRef;

    @NotNull
    private final List<ColumnValue> values;

    public ColumnInPredicateImpl(ColumnRef columnRef, List<ColumnValue> values) {
        super(Type.IN);
        this.columnRef = columnRef;
        this.values = values;
    }


    @Override
    public ColumnRef getColumnRef() {
        return columnRef;
    }

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
