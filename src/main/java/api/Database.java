package api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import api.exceptions.NoSuchTableException;
import api.exceptions.TableAlreadyExistsException;

public interface Database {

    String getName();

    @Nullable
    Table getTableOrNull(String tableName);

    @NotNull
    Table getTable(String tableName) throws NoSuchTableException;

    void createTable(TableMetadata description) throws TableAlreadyExistsException;

    void dropTable(String tableName);
}
