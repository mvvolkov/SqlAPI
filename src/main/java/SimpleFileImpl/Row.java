package SimpleFileImpl;

import sqlapi.exceptions.WrongValueTypeException;
import sqlapi.selectionPredicate.ColumnIsNullPredicate;
import sqlapi.selectionPredicate.SelectionPredicate;
import sqlapi.selectionPredicate.ColumnComparisonPredicate;
import sqlapi.selectionPredicate.CombinedPredicate;

import java.io.Serializable;
import java.util.Map;

public final class Row implements Serializable {


    private final Map<String, Value> values;

    public Row(Map<String, Value> values) {
        this.values = values;
    }

    public boolean evaluate(SelectionPredicate sc) throws WrongValueTypeException {

        if (sc.isEmpty()) {
            return true;
        }


        switch (sc.getType()) {
            case AND:
                CombinedPredicate cp1 = (CombinedPredicate) sc;
                return evaluate(cp1.getLeftPredicate()) &&
                        evaluate(cp1.getRightPredicate());
            case OR:
                CombinedPredicate cp2 = (CombinedPredicate) sc;
                return evaluate(cp2.getLeftPredicate()) ||
                        evaluate(cp2.getRightPredicate());
            case EQUALS:
            case NOT_EQUALS:
            case GREATER_THAN:
            case GREATER_THAN_OR_EQUALS:
            case LESS_THAN:
            case LESS_THAN_OR_EQUALS:
                ColumnComparisonPredicate bp = (ColumnComparisonPredicate) sc;
                return this.getValue(bp.getColumnReference().getColumnName()).evaluate(bp);
            case IS_NULL:
                ColumnIsNullPredicate cn1 = (ColumnIsNullPredicate) sc;
                return this.getValue(cn1.getColumnReference().getColumnName()).isNull();
            case IS_NOT_NULL:
                ColumnIsNullPredicate cn2 = (ColumnIsNullPredicate) sc;
                return this.getValue(cn2.getColumnReference().getColumnName()).isNotNull();
            default:
                return false;
        }
    }

    public Map<String, Value> getValues() {
        return values;
    }


    public Value getValue(String columnName) {
        return values.get(columnName);
    }
}
