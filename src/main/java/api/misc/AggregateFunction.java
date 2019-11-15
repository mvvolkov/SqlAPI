package api.misc;

public interface AggregateFunction extends SelectedItem {

    enum Type {
        COUNT,
        SUM,
        AVG,
        MAX,
        MIN
    }

    String getColumnName();

    String getAlias();

    Type getType();
}
