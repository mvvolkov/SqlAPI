package sqlapi.tables;

import sqlapi.misc.SelectedItem;

public interface DatabaseTableReference extends TableReference, SelectedItem {

    String getDatabaseName();

    String getSchemaName();

    String getTableName();

    @Override
    default TableRefType getTableRefType() {
        return TableRefType.DATABASE_TABLE;
    }
}
