package sqlapi.metadata;

public interface ColumnMetadata {

    String getColumnName();

    SqlType getSqlType();

    boolean isNotNull();

    boolean isPrimaryKey();

    default int getSize() {
        return -1;
    }

    Object getDefaultValue();

}
