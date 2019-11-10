package clientImpl.selectedItems;

public class SelectedAllImpl extends SelectedItemImpl {

    public SelectedAllImpl() {
        super(Type.SELECT_ALL);
    }

    @Override
    public String toString() {
        return "*";
    }
}
