package api;

public interface SelectedItem {

    enum Type {
        SELECT_ALL,
        SELECT_ALL_FROM_TABLE,
        SELECT_COLUMN_EXPRESSION
    }

    Type getType();
}
