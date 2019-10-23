package SimpleFileImpl;

import sqlapi.SelectionCriteria;

import java.util.Map;

public final class Row {

    private final Map<String, Value> values;

    public Row(Map<String, Value> values) {
        this.values = values;
    }

    public boolean evaluate(SelectionCriteria sc) throws Exception {

        switch (sc.getType()) {
            case AND:
                SelectionCriteria.CombinedPredicate cp1 = (SelectionCriteria.CombinedPredicate) sc;
                return evaluate(((SelectionCriteria.CombinedPredicate) sc).getLeft()) &&
                        evaluate(((SelectionCriteria.CombinedPredicate) sc).getRight());
            case OR:
                SelectionCriteria.CombinedPredicate cp2 = (SelectionCriteria.CombinedPredicate) sc;
                return evaluate(((SelectionCriteria.CombinedPredicate) sc).getLeft()) ||
                        evaluate(((SelectionCriteria.CombinedPredicate) sc).getRight());
            case EQUALS:
                SelectionCriteria.BinaryPredicate bp = (SelectionCriteria.BinaryPredicate) sc;
                return this.getValue(bp.getColumnReference().getColumnName()).evaluate(bp);

        }
        return false;
    }

    public Value getValue(String columnName) {
        return values.get(columnName);
    }

}
