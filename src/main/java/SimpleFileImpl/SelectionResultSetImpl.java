package SimpleFileImpl;

import sqlapi.selectionResult.SelectionResultRow;
import sqlapi.selectionResult.SelectionResultSet;

import java.util.ArrayList;
import java.util.List;

public class SelectionResultSetImpl implements SelectionResultSet {

    private final List<SelectionResultRow> rows;

    public SelectionResultSetImpl(List<SelectionResultRow> rows) {
        this.rows = rows;
    }


    @Override
    public List<SelectionResultRow> getAllRows() {
        return rows;
    }

    @Override
    public List<SelectionResultRow> getFirstRows(int numberOfRows) {
        return null;
    }
}
