package api;

import api.exceptions.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface SqlServer {

    void executeStatement(SqlStatement statement) throws SqlException;

    void createDatabase(String dbName) throws DatabaseAlreadyExistsException;

    void openDatabaseWithTables(String dbName, List<TableMetadata> tables);

    void persistDatabase(String dbName);


    @NotNull
    ResultSet select(SelectExpression selectExpression) throws WrongValueTypeException, NoSuchTableException, NoSuchDatabaseException, NoSuchColumnException;
}
