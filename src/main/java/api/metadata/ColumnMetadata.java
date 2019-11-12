package api.metadata;

public interface ColumnMetadata<V extends Comparable<V>> {

    String getColumnName();

    String getSqlTypeName();

    boolean isNotNull();

    boolean isPrimaryKey();

    Class<V> getJavaClass();

    default int getSize() {
        return -1;
    }

    Object getDefaultValue();

}
