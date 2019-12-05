package localFileDatabase.server.persistent;

import sqlapi.exceptions.*;
import sqlapi.metadata.ColumnMetadata;
import sqlapi.metadata.SqlType;

final class IntegerColumnMetadata extends PersistentColumnMetadata {

    IntegerColumnMetadata(ColumnMetadata columnMetadata, PersistentTable table) throws SqlException {
        super(columnMetadata, table);
    }

    @Override
    public SqlType getSqlType() {
        return SqlType.INTEGER;
    }

    @Override
    Integer getCheckedValue(Object value) throws SqlException {

        Integer newValue;
        if (value == null) {
            newValue = null;
        } else if (value instanceof Number) {
            newValue = ((Number) value).intValue();
        } else if (value instanceof String) {
            try {
                newValue = Integer.valueOf((String) value);
            } catch (NumberFormatException nfe) {
                throw new WrappedException(nfe);
            }
        } else {
            throw new WrongValueTypeException(Integer.class.getSimpleName(), value);
        }
        this.checkConstraints(newValue);
        return newValue;
    }

}
