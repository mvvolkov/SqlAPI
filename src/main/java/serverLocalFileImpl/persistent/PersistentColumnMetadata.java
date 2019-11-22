package serverLocalFileImpl.persistent;

import sqlapi.exceptions.WrongValueTypeException;
import sqlapi.metadata.ColumnConstraint;
import sqlapi.metadata.ColumnConstraintType;
import sqlapi.metadata.SqlType;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

public final class PersistentColumnMetadata implements Serializable {

    public static final long serialVersionUID = 1318768379416289869L;

    private final String columnName;

    private final SqlType sqlType;

    private final int size;

    private final Collection<ColumnConstraint> constraints;

    private final PersistentTable table;


    public PersistentColumnMetadata(String columnName, SqlType sqlType, int size, Collection<ColumnConstraint> constraints,
                                    PersistentTable table)
            throws WrongValueTypeException {
        this.columnName = columnName;
        this.sqlType = sqlType;
        this.constraints = constraints;
        this.size = size;
        this.table = table;
        for (ColumnConstraint constraint : constraints) {
            if (constraint.getConstraintType() == ColumnConstraintType.DEFAULT_VALUE) {
                this.checkValueType(constraint.getParameters().get(0));
            }
        }
    }

    public Collection<Class<?>> getAllowedJavaTypes() {
        switch (sqlType) {
            case INTEGER:
                return Collections.singletonList(Integer.class);
            case VARCHAR:
                return Collections.singletonList(String.class);
            default:
                return Collections.emptyList();
        }
    }

    public void checkValueType(Object value) throws WrongValueTypeException {
        if (value == null) {
            return;
        }
        for (Class cl : this.getAllowedJavaTypes()) {
            if (cl.isInstance(value)) {
                return;
            }
        }
        throw new WrongValueTypeException("dbo", table.getTableName(), columnName,
                this.getAllowedJavaTypes(), value.getClass());
    }

    public String getColumnName() {
        return columnName;
    }

    public int getSize() {
        return size;
    }

    public SqlType getSqlType() {
        return sqlType;
    }

    public Collection<ColumnConstraint> getConstraints() {
        return constraints;
    }

    public Object getDefaultValue() {
        for (ColumnConstraint constraint : constraints) {
            if (constraint.getConstraintType() == ColumnConstraintType.DEFAULT_VALUE) {
                return constraint.getParameters().get(0);
            }
        }
        return null;
    }


}
