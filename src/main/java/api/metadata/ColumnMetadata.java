package api.metadata;

public interface ColumnMetadata<V extends Comparable<V>> {

    String getName();

    boolean isNotNull();

    boolean isPrimaryKey();

    String getSqlTypeName();

    Class<V> getJavaClass();

}
