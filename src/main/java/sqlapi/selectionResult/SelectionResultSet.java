package sqlapi.selectionResult;

import java.util.List;

public interface SelectionResultSet {

    List<? extends SelectionResultRow> getAllRows();

    List<? extends SelectionResultRow> getFirstRows(int numberOfRows);
}
