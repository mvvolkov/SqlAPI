package SimpleFileImpl;

import sqlapi.exceptions.WrongValueTypeException;
import sqlapi.selectionPredicate.SelectionPredicate;
import sqlapi.selectionPredicate.ColumnComparisonPredicate;

import java.io.Serializable;

public final class Value<T extends Comparable<T> & Serializable> implements Serializable {

    private final T value;

    private final Class<T> type;

    public Value(Class<T> type, Object value) {
        this.type = type;
        this.value = this.type.cast(value);
    }

    public boolean evaluate(ColumnComparisonPredicate bp) throws WrongValueTypeException {

        if (value == null) {

        }


        Object obj = bp.getValue();

//        if (!javaClass.isInstance(obj)) {
//            throw new WrongValueTypeException(this.javaClass, value.getClass());
//        }
        T other = type.cast(obj);
        int compResult = value.compareTo(other);
        if (bp.getType().equals(SelectionPredicate.Type.EQUALS)) {
            return compResult == 0;
        }
        return false;
    }

    @Override
    public String toString() {
        return type.getSimpleName() + ": " + value;
    }


}
