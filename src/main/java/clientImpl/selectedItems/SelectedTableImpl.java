package clientImpl.selectedItems;

import api.selectedItems.SelectedTable;

public class SelectedTableImpl extends SelectedItemImpl implements SelectedTable {

    private final String dbName;
    private final String tableName;

    public SelectedTableImpl(String dbName, String tableName) {
        super(Type.SELECT_ALL_FROM_TABLE);
        this.dbName = dbName;
        this.tableName = tableName;
    }

    @Override
    public String toString() {
        return dbName + "." + tableName + ".*";
    }

    @Override
    public String getDatabaseName() {
        return dbName;
    }

    @Override
    public String getTableName() {
        return tableName;
    }
}
