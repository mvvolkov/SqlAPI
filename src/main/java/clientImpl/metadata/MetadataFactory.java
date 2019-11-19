package clientImpl.metadata;

import api.metadata.ColumnMetadata;
import api.metadata.TableMetadata;

import java.util.List;

public class MetadataFactory {

    public static ColumnMetadataImpl.Builder integerBuilder(
            String columnName) {
        return new ColumnMetadataImpl.Builder(columnName, ColumnMetadata.SqlType.INTEGER);
    }

    public static ColumnMetadataImpl.Builder varcharBuilder(String columnName,
                                                            int maxLength) {
        return new ColumnMetadataImpl.Builder(columnName, ColumnMetadata.SqlType.VARCHAR,
                maxLength);
    }

    public static TableMetadata tableMetadata(String tableName,
                                              List<ColumnMetadata> columnsMetadata) {
        return new TableMetadataImpl(tableName, columnsMetadata);
    }
}
