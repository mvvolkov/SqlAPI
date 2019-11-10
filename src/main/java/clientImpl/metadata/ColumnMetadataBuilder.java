package clientImpl.metadata;

import api.metadata.ColumnMetadata;

public interface ColumnMetadataBuilder<T extends ColumnMetadataBuilder<T>> {

    T notNull();

    T primaryKey();

    ColumnMetadata build();
}
