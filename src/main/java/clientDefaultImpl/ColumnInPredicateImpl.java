package clientDefaultImpl;

import api.ColumnReference;
import api.selectionPredicate.ColumnInPredicate;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ColumnInPredicateImpl extends SelectionPredicateImpl implements ColumnInPredicate {

    @NotNull
    private final ColumnReference columnReference;

    @NotNull
    private final List<?> values;

    public ColumnInPredicateImpl(ColumnReference columnReference, List<?> values) {
        super(Type.IN);
        this.columnReference = columnReference;
        this.values = values;
    }


    @Override
    public ColumnReference getColumnReference() {
        return columnReference;
    }

    @Override
    public List<?> getValues() {
        return values;
    }
}
