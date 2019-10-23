package SimpleFileImpl;

import sqlapi.selectionPredicate.AbstractPredicate;
import sqlapi.selectionPredicate.ColumnComparisonPredicate;
import sqlapi.selectionPredicate.CombinedPredicate;

import java.io.Serializable;
import java.util.Map;

public final class Row implements Serializable {

    private final Map<String, Value> values;

    public Row(Map<String, Value> values) {
        this.values = values;
    }

    public boolean evaluate(AbstractPredicate sc) throws Exception {

        switch (sc.getType()) {
            case AND:
                CombinedPredicate cp1 = (CombinedPredicate) sc;
                return evaluate(((CombinedPredicate) sc).getLeftPredicate()) &&
                        evaluate(((CombinedPredicate) sc).getRightPredicate());
            case OR:
                CombinedPredicate cp2 = (CombinedPredicate) sc;
                return evaluate(((CombinedPredicate) sc).getLeftPredicate()) ||
                        evaluate(((CombinedPredicate) sc).getRightPredicate());
            case EQUALS:
                ColumnComparisonPredicate bp = (ColumnComparisonPredicate) sc;
                return this.getValue(bp.getColumnReference().getColumnName()).evaluate(bp);

        }
        return false;
    }

    public Value getValue(String columnName) {
        return values.get(columnName);
    }
}
