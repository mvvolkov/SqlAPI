package sqlapi.selectionResult;

import sqlapi.exceptions.NoSuchColumnException;

public interface ResultRow {

    ResultValue getValue(String columnName) throws NoSuchColumnException;

    ResultValue getValueOrNull(String columnName);

    ResultValue getValue(int index);

    ResultValue getValueOrNull(int index);

    int getLength();

    int findColumn(String columnName);
}
