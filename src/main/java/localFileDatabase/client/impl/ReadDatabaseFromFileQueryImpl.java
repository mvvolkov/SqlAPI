package localFileDatabase.client.impl;

import org.jetbrains.annotations.NotNull;
import localFileDatabase.client.api.ReadDatabaseFromFileQuery;

final class ReadDatabaseFromFileQueryImpl extends DatabaseFileQueryImpl
        implements ReadDatabaseFromFileQuery {


    ReadDatabaseFromFileQueryImpl(@NotNull String fileName,
                                  @NotNull String databaseName) {
        super(fileName, databaseName);
    }


    @Override
    public String toString() {
        return "READ DATABASE " + databaseName + " FROM FILE " + fileName;
    }

}
