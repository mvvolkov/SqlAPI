package api;

public interface SelectionResultRow {
    SqlValue getValue(String columnName);
    SqlValue getValue(int index);
    int getLength();
    int findColumn(String columnName);
}
