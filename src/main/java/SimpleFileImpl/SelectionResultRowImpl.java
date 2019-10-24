package SimpleFileImpl;

import sqlapi.selectionResult.SelectionResultRow;
import sqlapi.selectionResult.SelectionResultValue;

import java.util.List;

public class SelectionResultRowImpl implements SelectionResultRow {

    private final List<SelectionResultValueImpl> values;

    public SelectionResultRowImpl(List<SelectionResultValueImpl> values) {
        this.values = values;
    }


    @Override
    public SelectionResultValue getValue(String columnName) {
        return null;
    }

    @Override
    public SelectionResultValue getValue(int index) {
        return null;
    }

    @Override
    public int getLength() {
        return values.size();
    }

    @Override
    public int findColumn(String columnName) {
        return 0;
    }
}
