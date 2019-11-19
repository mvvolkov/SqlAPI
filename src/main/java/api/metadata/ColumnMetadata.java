package api.metadata;

public interface ColumnMetadata {

    enum SqlType {
        INTEGER,
        VARCHAR
    }

    String getColumnName();

    SqlType getSqlType();

    boolean isNotNull();

    boolean isPrimaryKey();

    default int getSize() {
        return -1;
    }

    Object getDefaultValue();

}
