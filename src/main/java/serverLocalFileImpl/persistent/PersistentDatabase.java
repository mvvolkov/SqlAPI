package serverLocalFileImpl.persistent;

import serverLocalFileImpl.SqlServerImpl;
import sqlapi.exceptions.*;
import sqlapi.metadata.ColumnMetadata;
import sqlapi.metadata.TableMetadata;
import sqlapi.queries.*;
import sqlapi.selectResult.ResultRow;
import sqlapi.selectResult.ResultSet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class PersistentDatabase implements Serializable {

    public static final long serialVersionUID = -5620084460569971790L;

    private final String name;

    private final Collection<PersistentTable> tables = new ArrayList<>();

    private final transient SqlServerImpl sqlServer;

    public PersistentDatabase(String name, SqlServerImpl sqlServer) {
        this.name = name;
        this.sqlServer = sqlServer;
    }

    public String getName() {
        return name;
    }


    public void createTable(TableMetadata tableMetadata)
            throws TableAlreadyExistsException, WrongValueTypeException {

        PersistentTable table = this.getTableOrNull(tableMetadata.getTableName());
        if (table != null) {
            throw new TableAlreadyExistsException(name,
                    tableMetadata.getTableName());
        }
        tables.add(new PersistentTable(name, tableMetadata));
    }

    public void executeQuery(SqlTableQuery query) throws SqlException {
        if (query instanceof InsertQuery) {
            this.insert((InsertQuery) query);
            return;
        }
        if (query instanceof InsertFromSelectQuery) {
            this.insert((InsertFromSelectQuery) query);
            return;
        }
        if (query instanceof DeleteQuery) {
            this.delete((DeleteQuery) query);
            return;
        }
        if (query instanceof UpdateQuery) {
            this.update((UpdateQuery) query);
            return;
        }
        throw new InvalidQueryException("Invalid type of SQL query.");
    }


    private void insert(InsertQuery query)
            throws SqlException {
        this.getTable(query.getTableName()).insert(query.getColumns(), query.getValues());
    }

    private void insert(InsertFromSelectQuery query)
            throws SqlException {
        ResultSet resultSet =
                SqlServerImpl.createResultSet(
                        sqlServer.getInternalQueryResult(query.getSelectQuery()));
        for (ResultRow row : resultSet.getRows()) {
            this.getTable(query.getTableName())
                    .insert(query.getColumns(), row.getValues());
        }
    }


    private void delete(DeleteQuery query)
            throws SqlException {
        this.getTable(query.getTableName()).delete(query.getPredicate());
    }

    private void update(UpdateQuery query)
            throws SqlException {
        this.getTable(query.getTableName()).update(query);
    }


    private PersistentTable getTableOrNull(String tableName) {
        for (PersistentTable table : tables) {
            if (table.getTableName().equals(tableName)) {
                return table;
            }
        }
        return null;
    }


    public PersistentTable getTable(String tableName) throws NoSuchTableException {
        PersistentTable table = this.getTableOrNull(tableName);
        if (table == null) {
            throw new NoSuchTableException(tableName);
        }
        return table;
    }

    public void validate(Collection<TableMetadata> tableMetadataCollection)
            throws NoSuchTableException, NoSuchColumnException {

        for (TableMetadata tableMetadata : tableMetadataCollection) {
            PersistentTable table = this.getTable(tableMetadata.getTableName());
            table.validate(tableMetadata);
        }
    }
}
