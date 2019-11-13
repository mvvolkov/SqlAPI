package api.selectedItems;

public interface SelectedAll extends SelectedItem {

    @Override
    default Type getType() {
        return Type.SELECT_ALL;
    }
}
