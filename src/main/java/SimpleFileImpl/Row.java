package SimpleFileImpl;

import sqlapi.selectionPredicate.AbstractPredicate;
import sqlapi.selectionPredicate.BinaryPredicate;
import sqlapi.selectionPredicate.CombinedPredicate;

import java.util.Map;

public final class Row {

    private final Map<String, Value> values;

    public Row(Map<String, Value> values) {
        this.values = values;
    }

    public boolean evaluate(AbstractPredicate sc) throws Exception {

        switch (sc.getType()) {
            case AND:
                CombinedPredicate cp1 = (CombinedPredicate) sc;
                return evaluate(((CombinedPredicate) sc).getLeft()) &&
                        evaluate(((CombinedPredicate) sc).getRight());
            case OR:
                CombinedPredicate cp2 = (CombinedPredicate) sc;
                return evaluate(((CombinedPredicate) sc).getLeft()) ||
                        evaluate(((CombinedPredicate) sc).getRight());
            case EQUALS:
                BinaryPredicate bp = (BinaryPredicate) sc;
                return this.getValue(bp.getColumnReference().getColumnName()).evaluate(bp);

        }
        return false;
    }

    public Value getValue(String columnName) {
        return values.get(columnName);
    }

}
