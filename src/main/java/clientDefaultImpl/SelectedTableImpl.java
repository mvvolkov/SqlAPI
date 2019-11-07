package clientDefaultImpl;

public class SelectedTableImpl extends SelectedItemImpl {

    private final String table;

    public SelectedTableImpl(String table) {
        super(Type.SELECT_ALL_FROM_TABLE);
        this.table = table;
    }

    @Override
    public String toString() {
        return table + ".*";
    }
}
