package sqlapi.selectionResult;

import sqlapi.dbMetadata.ColumnMetadata;

import java.util.List;

public interface ResultSet {

    List<ColumnMetadata> getColumns();

    List<ResultRow> getAllRows();

    List<ResultRow> getFirstRows(int numberOfRows);

}
