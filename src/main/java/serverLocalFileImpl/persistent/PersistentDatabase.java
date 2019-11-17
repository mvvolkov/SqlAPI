package serverLocalFileImpl.persistent;

import api.exceptions.*;
import api.queries.*;
import api.selectResult.ResultRow;
import api.selectResult.ResultSet;
import org.jetbrains.annotations.NotNull;
import serverLocalFileImpl.SqlServerImpl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PersistentDatabase implements Serializable {

    public static final long serialVersionUID = -5620084460569971790L;

    private final String name;

    private final Collection<PersistentSchema> schemas = new ArrayList<>();

    private final PersistentSchema defaultSchema;

    private final transient SqlServerImpl sqlServer;

    public PersistentDatabase(String name, SqlServerImpl sqlServer) {
        this.name = name;
        this.sqlServer = sqlServer;
        defaultSchema = new PersistentSchema("dbo");
        schemas.add(defaultSchema);
    }

    public String getName() {
        return name;
    }

    private PersistentSchema getCurrentSchema() {
        return defaultSchema;
    }

    public void createSchema(String schemaName) throws DatabaseAlreadyExistsException {
        for (PersistentSchema schema : schemas) {
            if (schema.getName().equals(schemaName)) {
                throw new DatabaseAlreadyExistsException(schemaName);
            }
        }
        schemas.add(new PersistentSchema(schemaName));
    }

    public void executeStatement(SqlStatement stmt) throws SqlException {
        switch (stmt.getType()) {
            case CREATE_TABLE:
                this.createTable((CreateTableStatement) stmt);
                return;
            case INSERT:
                this.insert((InsertStatement) stmt);
                return;
            case INSERT_FROM_SELECT:
                this.insert((InsertFromSelectStatement) stmt);
                return;
            case DELETE:
                this.delete((DeleteStatement) stmt);
                return;
            case UPDATE:
                this.update((UpdateStatement) stmt);
                return;
        }
        throw new InvalidQueryException("Invalid type of SQL query.");
    }


    private void createTable(CreateTableStatement stmt)
            throws TableAlreadyExistsException, NoSuchSchemaException {
        PersistentSchema schema = this.getCurrentSchema();
        PersistentTable table = schema.getTableOrNull(stmt.getTableName());
        if (table != null) {
            throw new TableAlreadyExistsException(schema.getName(),
                    stmt.getTableName());
        }
        schema.addTable(
                new PersistentTable(schema.getName(), stmt.getTableName(),
                        stmt.getColumns()));
    }

    private void insert(InsertStatement stmt)
            throws SqlException {
        this.getTable(stmt).insert(stmt.getColumns(), stmt.getValues());
    }

    private void insert(InsertFromSelectStatement stmt)
            throws SqlException {
        ResultSet resultSet =
                SqlServerImpl.createResultSet(
                        sqlServer.getInternalQueryResult(stmt.getSelectExpression()));
        for (ResultRow row : resultSet.getRows()) {
            List<Object> values = new ArrayList<>();
            for (String column : resultSet.getColumns()) {
                values.add(row.getObject(column));
            }
            this.getTable(stmt).insert(stmt.getColumns(), values);
        }
    }


    private void delete(DeleteStatement stmt)
            throws NoSuchSchemaException, NoSuchTableException {
        this.getTable(stmt).delete(stmt.getPredicate());
    }

    private void update(UpdateStatement stmt)
            throws NoSuchSchemaException, NoSuchTableException {
        PersistentTable table = this.getTable(stmt);
        table.update(stmt);
    }

    private PersistentTable getTable(SqlStatement stmt)
            throws NoSuchSchemaException, NoSuchTableException {
        return this.getCurrentSchema().getTable(stmt.getTableName());
    }


    private @NotNull PersistentSchema getSchema(String schemaName)
            throws NoSuchSchemaException {
        for (PersistentSchema schema : schemas) {
            if (schema.getName().equals(schemaName)) {
                return schema;
            }
        }
        throw new NoSuchSchemaException(schemaName);
    }


    public PersistentTable getTable(String schemaName, String tableName) throws
            NoSuchTableException, NoSuchSchemaException {
        if (schemaName.isEmpty()) {
            schemaName = this.getCurrentSchema().getName();
        }
        for (PersistentSchema schema : schemas) {
            if (schema.getName().equals(schemaName)) {
                return schema.getTable(tableName);
            }
        }
        throw new NoSuchSchemaException(schemaName);
    }


}
