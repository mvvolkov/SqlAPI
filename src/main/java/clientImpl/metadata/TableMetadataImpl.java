package clientImpl.metadata;

import org.jetbrains.annotations.NotNull;
import sqlapi.metadata.ColumnMetadata;
import sqlapi.metadata.TableMetadata;

import java.util.List;
import java.util.stream.Collectors;

public final class TableMetadataImpl implements TableMetadata {

    @NotNull
    private final String tableName;

    @NotNull
    private final List<ColumnMetadata> columnMetadata;


    TableMetadataImpl(@NotNull String tableName,
                      @NotNull List<ColumnMetadata> columnMetadata) {
        this.tableName = tableName;
        this.columnMetadata = columnMetadata;
    }


    @NotNull
    @Override
    public List<ColumnMetadata> getColumnsMetadata() {
        return columnMetadata;
    }

    @NotNull
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
