package clientImpl.metadata;

import api.metadata.ColumnMetadata;
import api.metadata.TableMetadata;

import java.util.List;

public class MetadataFactory {

    public static ColumnMetadataImpl.Builder<Integer> integerBuilder(
            String columnName) {
        return new ColumnMetadataImpl.Builder<Integer>(columnName, "INTEGER",
                Integer.class);
    }

    public static ColumnMetadataImpl.Builder<String> varcharBuilder(String columnName,
                                                                    int maxLength) {
        return new ColumnMetadataImpl.Builder<String>(columnName, "VARCHAR", String.class,
                maxLength);
    }

    public static TableMetadata tableMetadata(String tableName,
                                              List<ColumnMetadata> columnsMetadata) {
        return new TableMetadataImpl(tableName, columnsMetadata);
    }
}
