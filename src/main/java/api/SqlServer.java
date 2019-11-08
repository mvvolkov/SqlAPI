package api;

import api.exceptions.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface SqlServer {

    void createDatabase(String dbName) throws DatabaseAlreadyExistsException;

    void openDatabaseWithTables(String dbName, List<TableMetadata> tables);

    void persistDatabase(String dbName);

    void executeStatement(SqlStatement statement) throws SqlException;

    @NotNull
    ResultSet select(SelectExpression selectExpression) throws SqlException;
}
