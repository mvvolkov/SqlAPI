package api.tables;

import api.SelectedItem;

public interface DatabaseTableReference extends TableReference, SelectedItem {

    String getDatabaseName();

    String getTableName();

    @Override
    default TableRefType getTableRefType() {
        return TableRefType.DATABASE_TABLE;
    }
}
