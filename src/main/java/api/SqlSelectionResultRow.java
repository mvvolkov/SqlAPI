package api;

public interface SqlSelectionResultRow {
    SqlSelectionResultValue getValue(String columnName);
    SqlSelectionResultValue getValue(int index);
    int getLength();
    int findColumn(String columnName);
}
