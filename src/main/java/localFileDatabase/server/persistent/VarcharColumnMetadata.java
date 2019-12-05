package localFileDatabase.server.persistent;


import org.jetbrains.annotations.Nullable;
import sqlapi.exceptions.ConstraintViolationException;
import sqlapi.exceptions.SqlException;
import sqlapi.metadata.ColumnConstraint;
import sqlapi.metadata.ColumnConstraintType;
import sqlapi.metadata.ColumnMetadata;
import sqlapi.metadata.SqlType;

final class VarcharColumnMetadata extends PersistentColumnMetadata {


    @Nullable
    private Integer maxSize;

    VarcharColumnMetadata(ColumnMetadata columnMetadata, PersistentTable table) throws SqlException {
        super(columnMetadata, table);
    }

    @Override
    public SqlType getSqlType() {
        return SqlType.VARCHAR;
    }

    private int retrieveMaxSize() {
        for (ColumnConstraint constraint : constraints) {
            if (constraint.getConstraintType() == ColumnConstraintType.MAX_SIZE) {
                return (int) constraint.getParameters().get(0);
            }
        }
        // May be we should throw an exception here as max size should always be set for VARCHAR
        return -1;
    }

    @Override
    String getCheckedValue(Object value) throws SqlException {
        String newValue = value == null ? null : String.valueOf(value);
        this.checkConstraints(newValue);
        return newValue;
    }

    private int getMaxSize() {
        if (maxSize == null) {
            maxSize = retrieveMaxSize();
        }
        return maxSize;
    }


    @Override
    protected final void checkMaxSize(Object newValue)
            throws ConstraintViolationException {

        if (newValue == null) {
            return;
        }

        String strValue = (String) newValue;
        if (strValue.length() > this.getMaxSize()) {
            throw new ConstraintViolationException(table.getDatabaseName(), table.getTableName(),
                    columnName, ColumnConstraintType.MAX_SIZE);
        }
    }
}
