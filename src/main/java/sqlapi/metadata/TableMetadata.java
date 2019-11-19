package sqlapi.metadata;

import java.util.List;

public interface TableMetadata {

    String getTableName();

    List<ColumnMetadata> getColumnsMetadata();
}
