package sqlapi.selectionPredicate;

import sqlapi.ColumnReference;

import java.util.List;
import java.util.stream.Collectors;

public class InPredicate extends AbstractPredicate {

    private final ColumnReference columnReference;

    private final List<?> values;

    public InPredicate(ColumnReference columnReference, List<?> values) {
        super(Type.IN);
        this.columnReference = columnReference;
        this.values = values;
    }


    @Override
    public String toString() {
        return columnReference + " " + this.getOperatorString() + " (" + values.stream()
                .map(obj -> getSqlString(obj)).collect(Collectors.joining(", "))
                + ")";
    }
}
