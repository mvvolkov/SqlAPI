package api.tables;

import api.misc.SelectedItem;

public interface DatabaseTableReference extends TableReference, SelectedItem {

    String getSchemaName();

    String getTableName();

    @Override
    default TableRefType getTableRefType() {
        return TableRefType.DATABASE_TABLE;
    }
}
