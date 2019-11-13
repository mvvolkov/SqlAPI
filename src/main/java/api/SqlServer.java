package api;

import api.exceptions.*;
import api.metadata.TableMetadata;
import api.queries.SelectExpression;
import api.queries.SqlStatement;
import api.selectResult.ResultSet;
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
