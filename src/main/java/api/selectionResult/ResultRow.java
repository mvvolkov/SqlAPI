package api.selectionResult;

import api.exceptions.NoSuchColumnException;

import java.util.List;

public interface ResultRow {

    List<ResultValue> getValues();

    ResultValue getValue(String columnName) throws NoSuchColumnException;

    ResultValue getValueOrNull(String columnName);

    ResultValue getValue(int index);

    ResultValue getValueOrNull(int index);

    int getLength();

    int findColumn(String columnName);
}
