package sqlapi;

import sqlapi.exceptions.ConstraintException;
import sqlapi.exceptions.WrongValueTypeException;

public interface ColumnMetadata<V extends Comparable<V>> {

    String getName();

    public boolean isNotNull();

    public boolean isPrimaryKey();

    String getTypeName();

    Class<V> getType();

    void checkConstraints(Table table, Object value)
            throws WrongValueTypeException, ConstraintException;
}
