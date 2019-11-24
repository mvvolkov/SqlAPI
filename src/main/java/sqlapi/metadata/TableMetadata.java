package sqlapi.metadata;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface TableMetadata {

    @NotNull String getTableName();

    @NotNull List<ColumnMetadata> getColumnsMetadata();
}
