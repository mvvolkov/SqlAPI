package localFileDatabase.client.impl;

import localFileDatabase.client.api.ReadDatabaseFromFileQuery;
import localFileDatabase.client.api.SaveDatabaseToFileQuery;
import org.jetbrains.annotations.NotNull;

public class FileQueryFactory {

    private FileQueryFactory() {
    }

    public static @NotNull ReadDatabaseFromFileQuery readDatabaseFromFile(
            @NotNull String fileName, @NotNull String databaseName) {
        return new ReadDatabaseFromFileQueryImpl(fileName, databaseName);
    }

    public static @NotNull SaveDatabaseToFileQuery saveDatabaseToFile(
            @NotNull String fileName,
            @NotNull String databaseName) {
        return new SaveDatabaseToFileQueryImpl(fileName, databaseName);
    }
}
