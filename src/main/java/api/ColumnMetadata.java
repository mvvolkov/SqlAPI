package api;

import api.exceptions.ConstraintException;
import api.exceptions.WrongValueTypeException;

public interface ColumnMetadata<V extends Comparable<V>> {

    String getName();

    public boolean isNotNull();

    public boolean isPrimaryKey();

    String getSqlTypeName();

    Class<V> getJavaClass();

    void checkConstraints(Table table, Object value)
            throws WrongValueTypeException, ConstraintException;
}
