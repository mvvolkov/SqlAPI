package localFileDatabase.server.persistent;

import sqlapi.exceptions.ConstraintViolationException;
import sqlapi.exceptions.SqlException;
import sqlapi.exceptions.UnsupportedColumnConstraintTypeException;
import sqlapi.metadata.ColumnConstraint;
import sqlapi.metadata.ColumnConstraintType;
import sqlapi.metadata.ColumnMetadata;
import sqlapi.metadata.SqlType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public abstract class PersistentColumnMetadata implements Serializable, ColumnMetadata {

    public static final long serialVersionUID = 1318768379416289869L;

    protected final String columnName;

    final Collection<PersistentColumnConstraint> constraints;

    protected final PersistentTable table;

    private final Object defaultValue;


    PersistentColumnMetadata(ColumnMetadata columnMetadata, PersistentTable table)
            throws SqlException {
        this.table = table;
        this.columnName = columnMetadata.getColumnName();
        this.constraints = columnMetadata.getConstraints()
                .stream().map(PersistentColumnConstraint::new)
                .collect(Collectors.toList());
        this.defaultValue = this.retrieveDefaultValue(columnMetadata.getConstraints());
    }

    @Override
    public String getColumnName() {
        return columnName;
    }


    @Override
    public Collection<ColumnConstraint> getConstraints() {
        return new ArrayList<>(constraints);
    }

    public PersistentColumnConstraint getColumnConstraintOrNull(
            ColumnConstraintType type) {
        for (PersistentColumnConstraint constraint : constraints) {
            if (constraint.getConstraintType() == type) {
                return constraint;
            }
        }
        return null;
    }

    @Override
    public abstract SqlType getSqlType();

    public Object getDefaultValue() {
        return defaultValue;
    }

    private Object retrieveDefaultValue(Collection<ColumnConstraint> constraints)
            throws SqlException {
        for (ColumnConstraint constraint : constraints) {
            if (constraint.getConstraintType() == ColumnConstraintType.DEFAULT_VALUE) {
                return this.getCheckedValue(constraint.getParameters().get(0));
            }
        }
        return null;
    }

    abstract Object getCheckedValue(Object newValue) throws SqlException;


    void checkConstraints(Object newValue)
            throws ConstraintViolationException,
            UnsupportedColumnConstraintTypeException {


        for (ColumnConstraint constraint : constraints) {
            ColumnConstraintType type = constraint.getConstraintType();
            switch (type) {
                case NOT_NULL:
                    this.checkNotNullConstraint(newValue, columnName, type);
                    break;
                case UNIQUE:
                    table.checkUniqueConstraint(newValue, columnName, type);
                    break;
                case PRIMARY_KEY:
                    this.checkNotNullConstraint(newValue, columnName, type);
                    table.checkUniqueConstraint(newValue, columnName, type);
                    break;
                case MAX_SIZE:
                    this.checkMaxSize(newValue);
                    break;
                case DEFAULT_VALUE:
                    break;
                default:
                    throw new UnsupportedColumnConstraintTypeException(type);
            }
        }
    }

    protected void checkMaxSize(Object newValue)
            throws ConstraintViolationException,
            UnsupportedColumnConstraintTypeException {
        throw new UnsupportedColumnConstraintTypeException(ColumnConstraintType.MAX_SIZE);
    }


    private void checkNotNullConstraint(Object newValue, String ColumnName,
                                        ColumnConstraintType type)
            throws ConstraintViolationException {
        if (newValue == null) {
            throw new ConstraintViolationException(table.getDatabaseName(),
                    table.getTableName(),
                    ColumnName, type);
        }
    }
}
