package localFileDatabase.client.impl;

import localFileDatabase.client.api.DatabaseFileQuery;
import org.jetbrains.annotations.NotNull;

abstract class DatabaseFileQueryImpl implements DatabaseFileQuery {

    @NotNull
    protected final String fileName;

    @NotNull
    protected final String databaseName;

    DatabaseFileQueryImpl(@NotNull String fileName,
                          @NotNull String databaseName) {
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
}
