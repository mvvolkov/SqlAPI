package sqlapi.selectionResult;

public interface SelectionResultRow {
    SelectionResultValue getValue(String columnName);
    SelectionResultValue getValue(int index);
    int getLength();
    int findColumn(String columnName);
}
