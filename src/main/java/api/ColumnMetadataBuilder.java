package api;

public interface ColumnMetadataBuilder<T extends ColumnMetadataBuilder<T>> {

    T notNull();

    T primaryKey();

    ColumnMetadata build();
}
