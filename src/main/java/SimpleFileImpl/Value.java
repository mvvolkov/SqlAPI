package SimpleFileImpl;

import sqlapi.exceptions.WrongValueTypeException;
import sqlapi.selectionPredicate.ColumnComparisonPredicate;

import java.io.Serializable;

public final class Value<T extends Comparable<T> & Serializable> implements Serializable {

    private final T value;

    private final Class<T> type;

    public Value(Class<T> type, Object value) {
        this.type = type;
        this.value = this.type.cast(value);
    }

    public boolean isNull() {
        return value == null;
    }

    public boolean isNotNull() {
        return value != null;
    }

    public boolean evaluate(ColumnComparisonPredicate bp) throws WrongValueTypeException {

        Object obj = bp.getValue();
        if (obj == null) {
            return false;
        }
        if (!type.isInstance(obj)) {
            throw new WrongValueTypeException(bp.getColumnReference(), this.type, obj.getClass());
        }
        if (value == null) {
            return false;
        }

        T other = type.cast(obj);
        int compResult = value.compareTo(other);
        switch (bp.getType()) {
            case EQUALS:
                return compResult == 0;
            case NOT_EQUALS:
                return compResult != 0;
            case GREATER_THAN:
                return compResult > 0;
            case GREATER_THAN_OR_EQUALS:
                return compResult > -0;
            case LESS_THAN:
                return compResult < 0;
            case LESS_THAN_OR_EQUALS:
                return compResult <= 0;
            default:
                return false;
        }
    }

    @Override
    public String toString() {
        return type.getSimpleName() + ": " + value;
    }


}
