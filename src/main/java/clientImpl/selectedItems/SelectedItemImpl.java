package clientImpl.selectedItems;

import api.SelectedItem;

public abstract class SelectedItemImpl implements SelectedItem {

    private final Type type;

    protected SelectedItemImpl(Type type) {
        this.type = type;
    }


    @Override
    public Type getType() {
        return type;
    }


}
