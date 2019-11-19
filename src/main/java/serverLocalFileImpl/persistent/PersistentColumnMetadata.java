package serverLocalFileImpl.persistent;

import sqlapi.exceptions.WrongValueTypeException;
import sqlapi.metadata.SqlType;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

public final class PersistentColumnMetadata implements Serializable {

    public static final long serialVersionUID = 1318768379416289869L;

    private final String columnName;

    private final SqlType sqlType;

    private final boolean isNotNull;

    private final boolean isPrimaryKey;

    private final int size;

    private final Object defaultValue;

    private final PersistentTable table;

    public PersistentColumnMetadata(String columnName, SqlType sqlType,
                                    boolean isNotNull,
                                    boolean isPrimaryKey, int size, Object defaultValue,
                                    PersistentTable table)
            throws WrongValueTypeException {
        this.columnName = columnName;
        this.sqlType = sqlType;
        this.isNotNull = isNotNull;
        this.isPrimaryKey = isPrimaryKey;
        this.size = size;
        this.table = table;
        this.checkValueType(defaultValue);
        this.defaultValue = defaultValue;

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


    public boolean isNotNull() {
        return isNotNull;
    }

    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    public int getSize() {
        return size;
    }

    public SqlType getSqlType() {
        return sqlType;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }


}
