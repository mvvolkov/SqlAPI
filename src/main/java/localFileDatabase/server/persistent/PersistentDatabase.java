package localFileDatabase.server.persistent;

import sqlapi.exceptions.NoSuchColumnException;
import sqlapi.exceptions.NoSuchTableException;
import sqlapi.exceptions.TableAlreadyExistsException;
import sqlapi.exceptions.WrongValueTypeException;
import sqlapi.metadata.TableMetadata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public final class PersistentDatabase implements Serializable {

    public static final long serialVersionUID = -5620084460569971790L;

    private final String name;

    private final Collection<PersistentTable> tables = new ArrayList<>();

    public PersistentDatabase(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Collection<TableMetadata> getTables() {
        return new ArrayList<>(tables);
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
