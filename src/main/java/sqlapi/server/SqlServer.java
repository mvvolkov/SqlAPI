package sqlapi.server;

import sqlapi.exceptions.*;
import sqlapi.metadata.TableMetadata;
import sqlapi.queries.SelectExpression;
import sqlapi.queries.SqlStatement;
import sqlapi.selectResult.ResultSet;

import java.util.List;

public interface SqlServer {

    void createDatabase(String dbName) throws DatabaseAlreadyExistsException;

    void createSchema(String dbName, String schemaName)
            throws NoSuchDatabaseException, SchemaAlreadyExistsException;

    void setCurrentSchema(String dbName, String schemaName)
            throws NoSuchDatabaseException, NoSuchSchemaException;

    void openDatabaseWithTables(String dbName, List<TableMetadata> tables);

    void persistDatabase(String dbName);

    void executeStatement(SqlStatement statement) throws SqlException;

    ResultSet getQueryResult(SelectExpression selectExpression) throws SqlException;

}
