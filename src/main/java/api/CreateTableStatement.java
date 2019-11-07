package api;

import java.util.List;

public interface CreateTableStatement extends SqlStatement {

    String getDatabaseName();

    String getTableName();

    List<ColumnMetadata> getColumns();

    @Override
    default Type getType() {
        return Type.CREATE_TABLE;
    }
}
