package clientImpl.metadata;

import org.jetbrains.annotations.NotNull;
import sqlapi.metadata.ColumnConstraint;
import sqlapi.metadata.ColumnConstraintType;
import sqlapi.metadata.ColumnMetadata;
import sqlapi.metadata.SqlType;

import java.util.Collection;

final class ColumnMetadataImpl implements ColumnMetadata {

    @NotNull
    private final String columnName;

    @NotNull
    private final SqlType sqlType;

    @NotNull
    private final Collection<ColumnConstraint> constraints;

    ColumnMetadataImpl(@NotNull String columnName, @NotNull SqlType sqlType,
                       @NotNull Collection<ColumnConstraint> constraints) {
        this.columnName = columnName;
        this.sqlType = sqlType;
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
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (ColumnConstraint constraint : constraints) {
            if (constraint.getConstraintType() == ColumnConstraintType.MAX_SIZE) {
                sb.insert(0, "(" + constraint.getParameters().get(0) + ")");
                continue;
            }
            sb.append(" ").append(constraint.toString());
        }

        return columnName + " " + sqlType + sb.toString();
    }


}
