package api;

import java.util.List;

public interface SqlSelectionResult {

    List<SqlSelectionResultRow> getAllRows();
    List<SqlSelectionResultRow> getFirstRows(int numberOfRows);
}
