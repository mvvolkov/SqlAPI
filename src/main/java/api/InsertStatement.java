package api;

import java.util.List;

public interface InsertStatement extends SqlStatement {

    String getDatabaseName();

    String getTableName();

    List<Object> getValues();

    List<String> getColumns();

    @Override
    default Type getType() {
        return Type.INSERT;
    }
}
