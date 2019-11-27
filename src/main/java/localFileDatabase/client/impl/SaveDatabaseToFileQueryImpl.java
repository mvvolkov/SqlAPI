package localFileDatabase.client.impl;

import org.jetbrains.annotations.NotNull;
import localFileDatabase.client.api.SaveDatabaseToFileQuery;

final class SaveDatabaseToFileQueryImpl implements SaveDatabaseToFileQuery {

    @NotNull
    private final String fileName;

    @NotNull
    private final String databaseName;

    public SaveDatabaseToFileQueryImpl(@NotNull String fileName, @NotNull String databaseName) {
        this.fileName = fileName;
        this.databaseName = databaseName;
    }

    @Override
    public @NotNull String getDatabaseName() {
        return databaseName;
    }

    @Override
    public @NotNull String getFileName() {
        return fileName;
    }

    @Override
    public String toString() {
        return "SAVE DATABASE " + databaseName + " TO FILE " + fileName;
    }

}
