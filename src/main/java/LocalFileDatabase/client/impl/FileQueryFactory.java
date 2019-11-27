package LocalFileDatabase.client.impl;

import org.jetbrains.annotations.NotNull;
import LocalFileDatabase.client.api.ReadDatabaseFromFileQuery;
import LocalFileDatabase.client.api.SaveDatabaseToFileQuery;
import sqlapi.metadata.TableMetadata;

import java.util.Collection;

public class FileQueryFactory {

    private FileQueryFactory() {
    }

    public static @NotNull ReadDatabaseFromFileQuery readDatabase(@NotNull String fileName, @NotNull String databaseName, @NotNull Collection<TableMetadata> tables) {
        return new ReadDatabaseFromFileQueryImpl(fileName, databaseName, tables);
    }

    public static @NotNull SaveDatabaseToFileQuery saveDatabase(@NotNull String fileName, @NotNull String databaseName) {
        return new SaveDatabaseToFileQueryImpl(fileName, databaseName);
    }
}
