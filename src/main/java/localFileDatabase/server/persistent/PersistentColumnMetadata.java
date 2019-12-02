package localFileDatabase.server.persistent;

import sqlapi.exceptions.NoSuchColumnException;
import sqlapi.exceptions.WrongValueTypeException;
import sqlapi.metadata.ColumnConstraint;
import sqlapi.metadata.ColumnConstraintType;
import sqlapi.metadata.ColumnMetadata;
import sqlapi.metadata.SqlType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

public final class PersistentColumnMetadata implements Serializable, ColumnMetadata {

    public static final long serialVersionUID = 1318768379416289869L;

    private final String columnName;

    private final SqlType sqlType;

    private final Collection<PersistentColumnConstraint> constraints;

    private final Object defaultValue;


    PersistentColumnMetadata(ColumnMetadata columnMetadata)
            throws WrongValueTypeException {
        this.columnName = columnMetadata.getColumnName();
        this.sqlType = columnMetadata.getSqlType();
        constraints = new ArrayList<>();
        Object defaultValue = null;
        for (ColumnConstraint constraint : columnMetadata.getConstraints()) {
            if (constraint.getConstraintType() == ColumnConstraintType.DEFAULT_VALUE) {
                defaultValue = constraint.getParameters().get(0);
                this.checkValueType(defaultValue);
            }
            constraints.add(new PersistentColumnConstraint(constraint));
        }
        this.defaultValue = defaultValue;
    }

    @Override
    public String getColumnName() {
        return columnName;
    }

    @Override
    public SqlType getSqlType() {
        return sqlType;
    }

    @Override
    public Collection<ColumnConstraint> getConstraints() {
        return new ArrayList<>(constraints);
    }

    int getSize() {
        for (ColumnConstraint constraint : constraints) {
            if (constraint.getConstraintType() == ColumnConstraintType.MAX_SIZE) {
                return (int) constraint.getParameters().get(0);
            }
        }
        return -1;
    }

    Object getDefaultValue() {
        return defaultValue;
    }

    void checkValueType(Object value) throws WrongValueTypeException {
        if (value == null) {
            return;
        }
        if (sqlType == SqlType.INTEGER) {
            if (value instanceof Number) {
                try {
                    BigDecimal bd = BigDecimal.valueOf(((Number) value).doubleValue());
                } catch (NumberFormatException nfe) {
                    throw new WrongValueTypeException();
                }
            }
            if (value instanceof String) {
                try {
                    BigDecimal bd = new BigDecimal(((String) value));
                } catch (NumberFormatException nfe) {
                    throw new WrongValueTypeException();
                }
            }
        }
        if (sqlType == SqlType.VARCHAR) {
            if (!(value instanceof String)) {
                throw new WrongValueTypeException();
            }
        }
    }


    private ColumnConstraint getConstraintOrNull(ColumnConstraintType type) {
        for (ColumnConstraint constraint : this.getConstraints()) {
            if (constraint.getConstraintType() == type) {
                return constraint;
            }
        }
        return null;
    }

    void validate(ColumnMetadata columnMetadata) throws NoSuchColumnException {
        String columnName = columnMetadata.getColumnName();
        if (columnMetadata.getSqlType() != this.getSqlType()) {
            throw new NoSuchColumnException(columnName);
        }
        if (columnMetadata.getConstraints().size() != this.getConstraints().size()) {
            throw new NoSuchColumnException(columnName);
        }
        for (ColumnConstraint constraint : columnMetadata.getConstraints()) {
            ColumnConstraintType type = constraint.getConstraintType();
            ColumnConstraint existConstraint = this.getConstraintOrNull(type);
            if (existConstraint == null) {
                throw new NoSuchColumnException(columnName);
            }
            if (type == ColumnConstraintType.MAX_SIZE) {
                int size = (int) constraint.getParameters().get(0);
                if (size != this.getSize()) {
                    throw new NoSuchColumnException(columnName);
                }
            }
            if (type == ColumnConstraintType.DEFAULT_VALUE) {
                Object defaultValue = constraint.getParameters().get(0);
                if (!defaultValue.equals(this.getDefaultValue())) {
                    throw new NoSuchColumnException(columnName);
                }
            }
        }
    }
}
