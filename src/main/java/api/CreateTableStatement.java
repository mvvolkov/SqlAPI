package api;

import java.util.List;

public interface CreateTableStatement extends SqlStatement {


    List<ColumnMetadata> getColumns();

    @Override
    default Type getType() {
        return Type.CREATE_TABLE;
    }
}