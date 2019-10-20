package sqlapi;

import sqlapi.exceptions.NoSuchTableException;
import sqlapi.exceptions.TableAlreadyExistsException;

public interface Database {

    String getName();

    Table getTableOrNull(String tableName);

    Table getTable(String tableName) throws NoSuchTableException;

    void createTable(TableMetadata description) throws TableAlreadyExistsException;

    void dropTable(String tableName);
}
