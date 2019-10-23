package SimpleFileImpl;

import sqlapi.SelectionCriteria;
import sqlapi.SelectionResultValue;
import sqlapi.exceptions.WrongValueTypeException;

import java.io.Serializable;

public final class Value<T extends Comparable<T> & Serializable> implements Serializable, SelectionResultValue {

    private final T value;

    private final Class<T> javaClass;

    public Value(Class<T> javaClass, Object value) {
        this.javaClass = javaClass;
        this.value = this.javaClass.cast(value);
    }

    public boolean evaluate(SelectionCriteria.BinaryPredicate bp) throws WrongValueTypeException {

        if (value == null){

        }


        Object obj = bp.getValue();

//        if (!javaClass.isInstance(obj)) {
//            throw new WrongValueTypeException(this.javaClass, value.getClass());
//        }
        T other = javaClass.cast(obj);
        int compResult = value.compareTo(other);
        return false;
    }

    @Override
    public String toString() {
        return javaClass.getSimpleName() + ": " + value;
    }

    @Override
    public String getColumnName() {
        return null;
    }

    @Override
    public Integer getInteger() {
//        if (!javaClass.isInstance(obj)) {
//            throw new WrongValueTypeException(this.javaClass, value.getClass());
//        }
        return (Integer) value;
    }

    @Override
    public String getString() {
        return null;
    }
}
