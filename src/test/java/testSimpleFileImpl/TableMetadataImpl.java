package testSimpleFileImpl;

import org.jetbrains.annotations.NotNull;
import sqlapi.ColumnMetadata;
import sqlapi.TableMetadata;

import java.util.List;
import java.util.stream.Collectors;

public final class TableMetadataImpl implements TableMetadata {

    @NotNull
    private final String name;

    @NotNull
    private final List<ColumnMetadata> columnMetadata;


    public TableMetadataImpl(@NotNull String name, @NotNull List<ColumnMetadata> columnMetadata) {
        this.name = name;
        this.columnMetadata = columnMetadata;
    }

    @NotNull
    public List<ColumnMetadata> getColumnMetadata() {
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
        return columnMetadata.stream().map(ColumnMetadata::toString).collect(Collectors.joining(", "));
    }
}
