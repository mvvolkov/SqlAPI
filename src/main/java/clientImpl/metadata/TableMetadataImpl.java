package clientImpl.metadata;

import org.jetbrains.annotations.NotNull;
import api.metadata.ColumnMetadata;
import api.metadata.TableMetadata;

import java.util.List;
import java.util.stream.Collectors;

public final class TableMetadataImpl implements TableMetadata {

    @NotNull
    private final String name;

    @NotNull
    private final List<ColumnMetadata> columnMetadata;


    public TableMetadataImpl(@NotNull String name,
                             @NotNull List<ColumnMetadata> columnMetadata) {
        this.name = name;
        this.columnMetadata = columnMetadata;
    }

    public static TableMetadata get(String tableName,
                                    List<ColumnMetadata> columnsMetadata) {
        return new TableMetadataImpl(tableName, columnsMetadata);
    }

    @NotNull
    public List<ColumnMetadata> getColumnsMetadata() {
        return columnMetadata;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + "(" + this.getColumnsDescriptionString() + ")";
    }

    private String getColumnsDescriptionString() {
        return columnMetadata.stream().map(ColumnMetadata::toString)
                .collect(Collectors.joining(", "));
    }
}
