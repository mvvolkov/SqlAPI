package localFileDatabase.client.impl;

import org.jetbrains.annotations.NotNull;
import localFileDatabase.client.api.SaveDatabaseToFileQuery;

final class SaveDatabaseToFileQueryImpl extends DatabaseFileQueryImpl
        implements SaveDatabaseToFileQuery {


    public SaveDatabaseToFileQueryImpl(@NotNull String fileName,
                                       @NotNull String databaseName) {
        super(fileName, databaseName);
    }


    @Override
    public String toString() {
        return "SAVE DATABASE " + databaseName + " TO FILE " + fileName;
    }

}
