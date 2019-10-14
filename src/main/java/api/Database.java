package api;

import api.exceptions.NoSuchTableException;
import api.exceptions.TableAlreadyExistsException;

public interface Database {

    String getName();

    Table getTableOrNull(String tableName);

    Table getTable(String tableName) throws NoSuchTableException;

    void createTable(TableDescription description) throws TableAlreadyExistsException;

    void dropTable(String tableName);
}
