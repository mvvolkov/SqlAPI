package api.selectionResult;

import api.ColumnMetadata;

import java.util.List;

public interface ResultSet {

    List<ColumnMetadata> getColumns();

    List<ResultRow> getAllRows();

    List<ResultRow> getFirstRows(int numberOfRows);

    ResultRow getRow(int index);

    int getSize();

}
