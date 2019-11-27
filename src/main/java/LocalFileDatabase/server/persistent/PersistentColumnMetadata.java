package LocalFileDatabase.server.persistent;

import sqlapi.exceptions.NoSuchColumnException;
import sqlapi.exceptions.WrongValueTypeException;
import sqlapi.metadata.ColumnConstraint;
import sqlapi.metadata.ColumnConstraintType;
import sqlapi.metadata.ColumnMetadata;
import sqlapi.metadata.SqlType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public final class PersistentColumnMetadata implements Serializable {

    public static final long serialVersionUID = 1318768379416289869L;

    private final String columnName;

    private final SqlType sqlType;


    private final Collection<PersistentColumnConstraint> constraints = new ArrayList<>();

    private final PersistentTable table;


    PersistentColumnMetadata(String columnName, SqlType sqlType,
                             Collection<ColumnConstraint> constraints,
                             PersistentTable table)
            throws WrongValueTypeException {
        this.columnName = columnName;
        this.sqlType = sqlType;
        this.table = table;
        for (ColumnConstraint constraint : constraints) {
            if (constraint.getConstraintType() == ColumnConstraintType.DEFAULT_VALUE) {
                this.checkValueType(constraint.getParameters().get(0));
            }
            this.constraints
                    .add(new PersistentColumnConstraint(constraint.getConstraintType(),
                            constraint.getParameters()));
        }
    }

    Collection<Class<?>> getAllowedJavaTypes() {
        switch (sqlType) {
            case INTEGER:
                return Collections.singletonList(Integer.class);
            case VARCHAR:
                return Collections.singletonList(String.class);
            default:
                return Collections.emptyList();
        }
    }

    void checkValueType(Object value) throws WrongValueTypeException {
        if (value == null) {
            return;
        }
        for (Class cl : this.getAllowedJavaTypes()) {
            if (cl.isInstance(value)) {
                return;
            }
        }
        throw new WrongValueTypeException(table.getTableName(), columnName,
                this.getAllowedJavaTypes(), value.getClass());
    }

    public String getColumnName() {
        return columnName;
    }

    public int getSize() {
        for (ColumnConstraint constraint : constraints) {
            if (constraint.getConstraintType() == ColumnConstraintType.MAX_SIZE) {
                return (int) constraint.getParameters().get(0);
            }
        }
        return -1;
    }

    SqlType getSqlType() {
        return sqlType;
    }

    Collection<PersistentColumnConstraint> getConstraints() {
        return constraints;
    }

    Object getDefaultValue() {
        for (ColumnConstraint constraint : constraints) {
            if (constraint.getConstraintType() == ColumnConstraintType.DEFAULT_VALUE) {
                return constraint.getParameters().get(0);
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
            ColumnConstraint existConstraint = null;
            for (ColumnConstraint constraint1 : this.getConstraints()) {
                if (constraint1.getConstraintType() == constraint.getConstraintType()) {
                    existConstraint = constraint1;
                    break;
                }
            }
            if (existConstraint == null) {
                throw new NoSuchColumnException(columnName);
            }
            if (existConstraint.getConstraintType() == ColumnConstraintType.MAX_SIZE) {
                int size1 = (int) existConstraint.getParameters().get(0);
                int size2 = (int) constraint.getParameters().get(0);
                if (size1 != size2) {
                    throw new NoSuchColumnException(columnName);
                }
            }
            if (existConstraint.getConstraintType() ==
                    ColumnConstraintType.DEFAULT_VALUE) {
                Object obj1 = existConstraint.getParameters().get(0);
                Object obj2 = constraint.getParameters().get(0);
                if (!obj1.equals(obj2)) {
                    throw new NoSuchColumnException(columnName);
                }
            }
        }
    }

    ColumnMetadata getColumnMetadata() {

        final Collection<ColumnConstraint> columnConstraints = constraints.stream().map(c -> (ColumnConstraint) c).collect(Collectors.toList());

        return new ColumnMetadata() {
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
                return columnConstraints;
            }
        };
    }

}
