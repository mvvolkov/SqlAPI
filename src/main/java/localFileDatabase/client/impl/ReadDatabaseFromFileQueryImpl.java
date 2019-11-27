package localFileDatabase.client.impl;

import org.jetbrains.annotations.NotNull;
import localFileDatabase.client.api.ReadDatabaseFromFileQuery;
import sqlapi.metadata.TableMetadata;

import java.util.Collection;
import java.util.stream.Collectors;

final class ReadDatabaseFromFileQueryImpl implements ReadDatabaseFromFileQuery {

    @NotNull
    private final String fileName;

    @NotNull
    private final String databaseName;

    @NotNull
    private final Collection<TableMetadata> tables;

    ReadDatabaseFromFileQueryImpl(@NotNull String fileName, @NotNull String databaseName,
                                  @NotNull Collection<TableMetadata> tables) {
        this.fileName = fileName;
        this.databaseName = databaseName;
        this.tables = tables;
    }

    @Override
    public @NotNull String getDatabaseName() {
        return databaseName;
    }

    @Override
    public @NotNull Collection<TableMetadata> getTables() {
        return tables;
    }

    @Override
    public @NotNull String getFileName() {
        return fileName;
    }

    @Override
    public String toString() {
        return "READ DATABASE " + databaseName + " WITH TABLES " + this.getTablesString() + " TO FILE " + fileName;
    }

    private String getTablesString() {
        return tables.stream().map(TableMetadata::toString).collect(Collectors.joining(", "));
    }
}
