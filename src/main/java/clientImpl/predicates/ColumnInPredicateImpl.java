package clientImpl.predicates;

import api.columnExpr.ColumnRef;
import api.columnExpr.ColumnValue;
import api.predicates.ColumnInPredicate;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ColumnInPredicateImpl extends SelectionPredicateImpl
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
}
