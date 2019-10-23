package sqlapi.selectionResult;

import java.util.List;

public interface SelectionResultSet {

    List<SelectionResultRow> getAllRows();
    List<SelectionResultRow> getFirstRows(int numberOfRows);
}
