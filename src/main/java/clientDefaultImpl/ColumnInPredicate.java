package clientDefaultImpl;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class ColumnInPredicate extends SelectionPredicateImpl {

    @NotNull
    private final ColumnReferenceImpl columnReference;

    @NotNull
    private final List<?> values;

    public ColumnInPredicate(ColumnReferenceImpl columnReference, List<?> values) {
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
