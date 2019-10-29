package sqlapi;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sqlapi.exceptions.NoSuchTableException;
import sqlapi.exceptions.TableAlreadyExistsException;

public interface Database {

    String getName();

    @Nullable
    Table getTableOrNull(String tableName);

    @NotNull
    Table getTable(String tableName) throws NoSuchTableException;

    void createTable(TableMetadata description) throws TableAlreadyExistsException;

    void dropTable(String tableName);
}
