package localFileDatabase.server.persistent;

import clientImpl.columnExpr.ColumnExprFactory;
import sqlapi.columnExpr.ColumnValue;
import sqlapi.exceptions.*;
import sqlapi.metadata.TableMetadata;
import sqlapi.queries.*;
import sqlapi.queryResult.QueryResultRow;
import sqlapi.queryResult.QueryResult;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class PersistentDatabase implements Serializable {

    public static final long serialVersionUID = -5620084460569971790L;

    private final String name;

    private final Collection<PersistentTable> tables = new ArrayList<>();

    public PersistentDatabase(String name) {
        this.name = name;
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

    public void dropTable(String tableName) throws NoSuchTableException {
        PersistentTable table = this.getTable(tableName);
        tables.remove(table);
    }

    public void executeQuery(TableActionQuery query) throws SqlException {
        if (query instanceof InsertQuery) {
            this.insert((InsertQuery) query);
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
        throw new UnsupportedQueryTypeException(query);
    }


    private void insert(InsertQuery query)
            throws SqlException {
        this.getTable(query.getTableName()).insert(query.getColumns(), query.getValues());
    }

    public void insert(InsertFromSelectQuery query, QueryResult queryResult)
            throws SqlException {
        for (QueryResultRow row : queryResult.getRows()) {
            List<ColumnValue> values = new ArrayList<>();
            for (Object value : row.getValues()) {
                values.add(ColumnExprFactory.value(value));
            }
            this.getTable(query.getTableName())
                    .insert(query.getColumns(), values);
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

    public Collection<TableMetadata> getTables() {
        return tables.stream().map(PersistentTable::getTableMetadata)
                .collect(Collectors.toList());
    }
}
