package clientImpl.metadata;

import org.jetbrains.annotations.NotNull;
import sqlapi.metadata.ColumnConstraint;
import sqlapi.metadata.ColumnMetadata;
import sqlapi.metadata.SqlType;

import java.util.Collection;

public final class ColumnMetadataImpl implements ColumnMetadata {

    @NotNull
    private final String columnName;

    @NotNull
    private final SqlType sqlType;

    private final int size;

    @NotNull
    private final Collection<ColumnConstraint> constraints;

    public ColumnMetadataImpl(String columnName, SqlType sqlType, int size, Collection<ColumnConstraint> constraints) {
        this.columnName = columnName;
        this.sqlType = sqlType;
        this.size = size;
        this.constraints = constraints;
    }


    @NotNull
    @Override
    public String getColumnName() {
        return columnName;
    }

    @NotNull
    @Override
    public SqlType getSqlType() {
        return sqlType;
    }

    @NotNull
    @Override
    public Collection<ColumnConstraint> getConstraints() {
        return constraints;
    }

    @Override
    public int getSize() {
        return size;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(columnName);
        sb.append(" ");
        sb.append(this.getSqlType());
        if (this.getSize() != -1) {
            sb.append("(");
            sb.append(this.getSize());
            sb.append(")");
        }
        for (ColumnConstraint constraint : constraints) {
            switch (constraint.getConstraintType()) {
                case NOT_NULL:
                    sb.append(" NOT NULL");
                    break;
                case PRIMARY_KEY:
                    sb.append(" PRIMARY KEY");
                    break;
                case DEFAULT_VALUE:
                    sb.append(" DEFAULT ");
                    sb.append(constraint.getParameters().get(0));
                default:
                    // do nothing
            }
        }
        return sb.toString();
    }


}
