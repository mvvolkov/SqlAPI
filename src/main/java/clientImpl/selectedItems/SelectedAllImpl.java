package clientImpl.selectedItems;

import api.selectedItems.SelectedAll;

public class SelectedAllImpl extends SelectedItemImpl implements SelectedAll {

    public SelectedAllImpl() {
        super(Type.SELECT_ALL);
    }

    @Override
    public String toString() {
        return "*";
    }
}
