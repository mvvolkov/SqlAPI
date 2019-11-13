package api.selectedItems;

public interface SelectedTable extends SelectedItem {

    String getDatabaseName();

    String getTableName();

    @Override
    default Type getType() {
        return Type.SELECT_ALL_FROM_TABLE;
    }
}
