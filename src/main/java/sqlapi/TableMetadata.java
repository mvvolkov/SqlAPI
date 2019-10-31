package sqlapi;

import java.util.List;

public interface TableMetadata {

    String getName();

    List<ColumnMetadata> getColumnsMetadata();
}
