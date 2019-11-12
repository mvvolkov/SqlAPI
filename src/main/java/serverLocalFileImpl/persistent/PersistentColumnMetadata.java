package serverLocalFileImpl.persistent;

import java.io.Serializable;

public final class PersistentColumnMetadata implements Serializable {

    public static final long serialVersionUID = 1318768379416289869L;

    private final String columnName;

    private final String sqlTypeName;

    private final Class javaClass;

    private final boolean isNotNull;

    private final boolean isPrimaryKey;

    private final int size;

    public PersistentColumnMetadata(String columnName, String sqlTypeName,
                                    Class javaClass, boolean isNotNull,
                                    boolean isPrimaryKey, int size) {
        this.columnName = columnName;
        this.sqlTypeName = sqlTypeName;
        this.javaClass = javaClass;
        this.isNotNull = isNotNull;
        this.isPrimaryKey = isPrimaryKey;
        this.size = size;
    }

    public String getColumnName() {
        return columnName;
    }

    public Class getJavaClass() {
        return javaClass;
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

    public String getSqlTypeName() {
        return sqlTypeName;
    }
}
