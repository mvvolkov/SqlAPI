package clientImpl.metadata;

import org.jetbrains.annotations.NotNull;
import api.metadata.ColumnMetadata;
import api.metadata.TableMetadata;

import java.util.List;
import java.util.stream.Collectors;

public final class TableMetadataImpl implements TableMetadata {

    @NotNull
    private final String tableName;

    @NotNull
    private final List<ColumnMetadata> columnMetadata;


    public TableMetadataImpl(@NotNull String tableName,
                             @NotNull List<ColumnMetadata> columnMetadata) {
        this.tableName = tableName;
        this.columnMetadata = columnMetadata;
    }


    @NotNull
    public List<ColumnMetadata> getColumnsMetadata() {
        return columnMetadata;
    }

    @Override
    public String getTableName() {
        return tableName;
    }

    @Override
    public String toString() {
        return tableName + "(" + this.getColumnsDescriptionString() + ")";
    }

    private String getColumnsDescriptionString() {
        return columnMetadata.stream().map(ColumnMetadata::toString)
                .collect(Collectors.joining(", "));
    }
}
